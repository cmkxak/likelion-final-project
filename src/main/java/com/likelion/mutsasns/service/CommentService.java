package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.dto.request.comment.CommentRequest;
import com.likelion.mutsasns.domain.dto.response.comment.CommentCreateResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentDeleteResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentModifyResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentResponse;
import com.likelion.mutsasns.domain.entity.Alarm;
import com.likelion.mutsasns.domain.entity.Comment;
import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.enumerate.AlarmType;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.repository.AlarmRepository;
import com.likelion.mutsasns.repository.CommentRepository;
import com.likelion.mutsasns.repository.PostRepository;
import com.likelion.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private static final String DELETE_COMMENT_MESSAGE = "댓글 삭제 완료";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> findAll(Integer postId, Pageable pageable) {
        validatePostExists(postId);
        return CommentResponse.of(commentRepository.findAllByPostId(postId, pageable));
    }

    public CommentCreateResponse createComment(Integer postId, CommentRequest request, String userName) {
        Post post = findPost(postId);
        User user = findUser(userName);
        Comment savedComment = commentRepository.save(Comment.createComment(request.getComment(), post, user));
        saveNewCommentAlarm(postId, user);
        return CommentCreateResponse.of(savedComment);
    }

    public CommentModifyResponse updateComment(Integer postId, Integer commentId, CommentRequest request, String userName) {
        validatePostExists(postId);
        Comment comment = findCommentByUserAuthority(commentId, userName);
        comment.updateComment(request.getComment());
        return CommentModifyResponse.of(comment);
    }

    public CommentDeleteResponse deleteComment(Integer postId, Integer commentId, String userName) {
        validatePostExists(postId);
        if (isAuthorizedUser(commentId, userName)){
            commentRepository.deleteById(commentId);
        }
        return CommentDeleteResponse.of(DELETE_COMMENT_MESSAGE, commentId);
    }

    private void saveNewCommentAlarm(Integer postId, User user) {
        alarmRepository.save(Alarm.createAlarm(postId, user.getId(), AlarmType.NEW_COMMENT_ON_POST.getMessage(), AlarmType.NEW_COMMENT_ON_POST));
    }

    private Comment findCommentByUserAuthority(Integer commentId, String userName) {
        User findUser = findUser(userName);
        Comment comment = findComment(commentId);
        if (Objects.equals(comment.getUser().getId(), findUser.getId()))
            return comment;
        else throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
    }

    private boolean isAuthorizedUser(Integer commentId, String userName){
        User findUser = findUser(userName);
        Comment comment = findComment(commentId);
        if (Objects.equals(comment.getUser().getId(), findUser.getId()))
            return true;
        else throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
    }

    private void validatePostExists(Integer postId){
        if(!postRepository.existsById(postId)){
            throw new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage());
        }
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    private Comment findComment(Integer commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new AppException(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getMessage()));
    }

    private Post findPost(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }
}
