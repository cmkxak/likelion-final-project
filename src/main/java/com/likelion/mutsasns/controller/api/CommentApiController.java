package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.request.comment.CommentRequest;
import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.comment.CommentCreateResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentDeleteResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentModifyResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentResponse;
import com.likelion.mutsasns.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> findAllComments(
            @PathVariable("postId") Integer postId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return Response.success(commentService.findAll(postId, pageable));
    }

    @PostMapping("/{postId}/comments")
    public Response<CommentCreateResponse> createComment(
            @PathVariable("postId") Integer postId,
            @Valid @RequestBody CommentRequest request,
            @ApiIgnore Authentication authentication) {
        log.info(request.toString());
        return Response.success(commentService.createComment(postId, request, authentication.getName()));
    }

    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentModifyResponse> updateComment(
            @PathVariable("postId") Integer postId,
            @PathVariable("id") Integer commentId,
            @Valid @RequestBody CommentRequest request,
            @ApiIgnore Authentication authentication) {
        log.info(request.toString());
        return Response.success(commentService.updateComment(postId, commentId, request, authentication.getName()));
    }

    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentDeleteResponse> deleteComment(
            @PathVariable("postId") Integer postId,
            @PathVariable("id") Integer commentId,
            @ApiIgnore Authentication authentication) {
        return Response.success(commentService.deleteComment(postId, commentId, authentication.getName()));
    }
}
