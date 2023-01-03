package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.request.comment.CommentRequest;
import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.comment.CommentDeleteResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentResponse;
import com.likelion.mutsasns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    //댓글 목록 조회
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> findAllComments(
            @PathVariable("postId") Integer postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(commentService.findAll(postId, pageable));
    }

    //댓글 작성
    @PostMapping("/{postId}/comments")
    public Response<CommentResponse> createComment(
            @PathVariable("postId") Integer postId,
            @RequestBody CommentRequest request,
            @ApiIgnore Authentication authentication) {
        return Response.success(commentService.createComment(postId, request, authentication.getName()));
    }

    //댓글 수정
    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> updateComment(
            @PathVariable("postId") Integer postId,
            @PathVariable("id") Integer commentId,
            @RequestBody CommentRequest request,
            @ApiIgnore Authentication authentication) {
        return Response.success(commentService.updateComment(postId, commentId, request, authentication.getName()));
    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentDeleteResponse> deleteComment(
            @PathVariable("postId") Integer postId,
            @PathVariable("id") Integer commentId,
            @ApiIgnore Authentication authentication) {
        return Response.success(commentService.deleteComment(postId, commentId, authentication.getName()));
    }

}
