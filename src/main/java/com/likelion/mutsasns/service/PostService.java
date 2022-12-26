package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.dto.request.post.PostSaveRequest;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final String SUCCESS_MESSAGE = "포스트 등록 완료";
    private final String UPDATE_MESSAGE = "포스트 수정 완료";
    private final String DELETE_MESSAGE = "포스트 삭제 완료";
    private final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다.";

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponse> findAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream().map(Post::toResponse).collect(Collectors.toList());
    }

    public PostResponse findOne(Integer id) {
        Post findPost = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ""));
        return findPost.toResponse();
    }

    @Transactional
    public PostSaveResponse createPost(PostSaveRequest request, String userName) {
        User findUser = validateUserByToken(userName);

        Post post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(findUser)
                .build();

        Post savedPost = postRepository.save(post);

        return new PostSaveResponse(SUCCESS_MESSAGE, savedPost.getId());
    }


    @Transactional
    public PostSaveResponse updatePost(Integer postId, PostSaveRequest request, String userName) {
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
        //올바른 유저 인지 검증
        User findUser = userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.INVALID_TOKEN, INVALID_TOKEN_MESSAGE));
        //포스트 검증
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        //유저의 id와 게시글 id가 같으면.
        if (post.getUser().getId() == findUser.getId()) {
            return true;
        } else {
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage());
        }
    }

    private User validateUserByToken(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.INVALID_TOKEN, INVALID_TOKEN_MESSAGE));
    }
}