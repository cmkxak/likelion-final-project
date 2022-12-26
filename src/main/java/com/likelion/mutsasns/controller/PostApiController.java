package com.likelion.mutsasns.controller;

import com.likelion.mutsasns.domain.dto.request.post.PostSaveRequest;
import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.post.PostResponse;
import com.likelion.mutsasns.domain.dto.response.post.PostSaveResponse;
import com.likelion.mutsasns.service.PostService;
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

    @GetMapping
    public Response<Page<PostResponse>> findAll(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return Response.success(postService.findAllPost(pageable));
    }

    @GetMapping("/{id}")
    public Response<PostResponse> findOne(@PathVariable Integer id){
        return Response.success(postService.findOne(id));
    }

    @PostMapping
    public Response<PostSaveResponse> save(@RequestBody PostSaveRequest request, @ApiIgnore Authentication authentication){
        return Response.success(postService.createPost(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public Response<PostSaveResponse> update(@PathVariable Integer id, @RequestBody PostSaveRequest request, @ApiIgnore Authentication authentication){
        return Response.success(postService.updatePost(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public Response<PostSaveResponse> delete(@PathVariable Integer id, @ApiIgnore Authentication authentication){
        return Response.success(postService.deletePost(id, authentication.getName()));
    }
}
