package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.request.post.PostRequest;
import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.post.PostResponse;
import com.likelion.mutsasns.domain.dto.response.post.PostSaveResponse;
import com.likelion.mutsasns.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class PostApiController {

    private final PostService postService;

    @ApiOperation(value = "게시글 리스트 조회", notes = "모든 게시글을 조회한다.")
    @GetMapping
    public Response<Page<PostResponse>> findAll(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(postService.findAllPost(pageable));
    }

    @ApiOperation(value = "게시글 상세 조회", notes = "포스트 ID를 통해 하나의 게시글을 조회한다.")
    @GetMapping("/{id}")
    public Response<PostResponse> findOne(@PathVariable Integer id) {
        return Response.success(postService.findOne(id));
    }

    @ApiOperation(value = "마이 피드 조회", notes = "내가 남긴 게시글만을 조회한다.")
    @GetMapping("/my")
    public Response findOwn(@ApiIgnore Authentication authentication, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(postService.findOwn(authentication.getName(), pageable));
    }

    @ApiOperation(value = "게시글 저장", notes = "게시글을 생성한다.")
    @PostMapping
    public Response<PostSaveResponse> save(@RequestBody PostRequest request, @ApiIgnore Authentication authentication) {
        return Response.success(postService.createPost(request, authentication.getName()));
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글을 수정한다.")
    @PutMapping("/{id}")
    public Response<PostSaveResponse> update(@PathVariable Integer id, @RequestBody PostRequest request, @ApiIgnore Authentication authentication) {
        return Response.success(postService.updatePost(id, request, authentication.getName()));
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다.")
    @DeleteMapping("/{id}")
    public Response<PostSaveResponse> delete(@PathVariable Integer id, @ApiIgnore Authentication authentication) {
        return Response.success(postService.deletePost(id, authentication.getName()));
    }
}
