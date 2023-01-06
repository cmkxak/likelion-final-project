package com.likelion.mutsasns.domain.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.mutsasns.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentCreateResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static CommentCreateResponse of(Comment savedComment) {
        return CommentCreateResponse.builder()
                .comment(savedComment.getComment())
                .userName(savedComment.getUser().getUserName())
                .postId(savedComment.getPost().getId())
                .build();
    }
}
