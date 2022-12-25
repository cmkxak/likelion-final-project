package com.likelion.mutsasns.domain.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostSaveRequest {
    private String title;
    private String body;
}
