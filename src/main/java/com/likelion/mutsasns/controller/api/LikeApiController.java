package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.service.LikeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class LikeApiController {

    private final LikeService likeService;

    @ApiOperation(value = "좋아요 생성", notes = "특정 게시글에 좋아요를 누른다.")
    @PostMapping("/{postId}/likes")
    public Response<String> createLike(@PathVariable("postId") Integer postId, @ApiIgnore Authentication authentication) {
        return Response.success(likeService.createLike(postId, authentication.getName()));
    }

    @ApiOperation(value = "좋아요 수 세기", notes = "특정 게시글에 눌린 좋아요 개수를 조회한다.")
    @GetMapping("/{postId}/likes")
    public Response<Long> countLike(@PathVariable("postId") Integer postId) {
        return Response.success(likeService.countLike(postId));
    }
}
