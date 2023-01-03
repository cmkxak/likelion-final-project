package com.likelion.mutsasns.domain.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentDeleteResponse {
    private String message;
    private Integer id;

    public static CommentDeleteResponse of(String message, Integer commentId) {
        return CommentDeleteResponse.builder()
                .message(message)
                .id(commentId)
                .build();

    }
}
