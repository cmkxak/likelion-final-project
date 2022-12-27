package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.dto.request.post.PostRequest;
import com.likelion.mutsasns.domain.dto.response.post.PostResponse;
import com.likelion.mutsasns.domain.dto.response.post.PostSaveResponse;
import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.repository.PostRepository;
import com.likelion.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private static final String SUCCESS_MESSAGE = "포스트 등록 완료";
    private static final String UPDATE_MESSAGE = "포스트 수정 완료";
    private static final String DELETE_MESSAGE = "포스트 삭제 완료";

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Page<PostResponse> findAllPost(Pageable pageable) {
        return PostResponse.of(postRepository.findAll(pageable));
    }

    public PostResponse findOne(Integer id) {
        Post findPost = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ""));
        return PostResponse.of(findPost);
    }

    @Transactional
    public PostSaveResponse createPost(PostRequest request, String userName) {
        User findUser = validateUserByToken(userName);

        Post newPost = Post.createPost(request.getTitle(), request.getBody(), findUser);

        Post savedPost = postRepository.save(newPost);

        return new PostSaveResponse(SUCCESS_MESSAGE, savedPost.getId());
    }


    @Transactional
    public PostSaveResponse updatePost(Integer postId, PostRequest request, String userName) {
        validateCorrectPost(postId, userName);

        Post findPost = postRepository.findById(postId).orElseThrow(() ->
                new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        findPost.updatePost(request.getTitle(), request.getBody());

        return new PostSaveResponse(UPDATE_MESSAGE, findPost.getId());
    }

    @Transactional
    public PostSaveResponse deletePost(Integer postId, String userName) {
        validateCorrectPost(postId, userName);

        try {
            postRepository.deleteById(postId);
        } catch (AppException e) {
            e.printStackTrace();
        }

        return new PostSaveResponse(DELETE_MESSAGE, postId);
    }

    private boolean validateCorrectPost(Integer postId, String userName) {
        User findUser = userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        if (post.getUser().getId() == findUser.getId()) {
            return true;
        } else {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    private User validateUserByToken(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }
}
