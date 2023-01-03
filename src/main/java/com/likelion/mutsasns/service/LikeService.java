package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.entity.Like;
import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.repository.LikeRepository;
import com.likelion.mutsasns.repository.PostRepository;
import com.likelion.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {

    private static final String SUCCESS_LIKE_MESSAGE = "좋아요를 눌렀습니다.";
    private static final String DUPLICATE_LIKE_MESSAGE = "이미 좋아요를 누른 유저입니다.";

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createLike(Integer postId, String userName) {
        validateDuplicateLike(findUser(userName), findPost(postId));
        likeRepository.save(Like.createLike(findPost(postId), findUser(userName)));
        return SUCCESS_LIKE_MESSAGE;
    }

    public Long countLike(Integer postId) {
        return likeRepository.countByPost(findPost(postId));
    }

    private void validateDuplicateLike(User user, Post post) {
        likeRepository.findByUserAndPost(user, post).ifPresent(like -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_NAME, DUPLICATE_LIKE_MESSAGE);
        });
    }

    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    private Post findPost(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }
}
