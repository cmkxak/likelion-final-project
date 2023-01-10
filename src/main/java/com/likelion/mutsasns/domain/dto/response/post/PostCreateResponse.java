package com.likelion.mutsasns.domain.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostCreateResponse {
    private String message;
    private Integer postId;
}
