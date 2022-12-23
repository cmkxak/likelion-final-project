package com.likelion.mutsasns.domain.dto.request.post;

import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostSaveRequest {
    private String title;
    private String body;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .body(body)
                .build();
    }
}
