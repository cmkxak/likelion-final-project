package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/{postId}/likes")
    public Response<String> createLike(@PathVariable("postId") Integer postId, @ApiIgnore Authentication authentication) {
        return Response.success(likeService.createLike(postId, authentication.getName()));
    }

    @GetMapping("/{postId}/likes")
    public Response<Long> countLike(@PathVariable("postId") Integer postId) {
        return Response.success(likeService.countLike(postId));
    }
}
