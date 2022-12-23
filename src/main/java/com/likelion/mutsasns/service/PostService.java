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
    private static final String SUCCESS_MESSAGE = "포스트 등록 완료";
    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다.";

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<PostResponse> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.stream().map(Post::toResponse).collect(Collectors.toList());
    }
    
    public PostResponse findOne(Integer id) {
        Post findPost = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ""));
        return findPost.toResponse();
    }

    @Transactional
    public PostSaveResponse save(PostSaveRequest request, String userName) {
        User findUser = userRepository.findByUserName(userName).orElseThrow(() ->
                new AppException(ErrorCode.INVALID_TOKEN, INVALID_TOKEN_MESSAGE));

        Post post = Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .user(findUser)
                .build();

        Post savedPost = postRepository.save(post);

        return PostSaveResponse.builder()
                .postId(savedPost.getId())
                .message(SUCCESS_MESSAGE)
                .build();
    }
}
