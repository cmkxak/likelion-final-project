package com.likelion.mutsasns.domain.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateResponse {
    private String message;
    private Integer postId;
}
