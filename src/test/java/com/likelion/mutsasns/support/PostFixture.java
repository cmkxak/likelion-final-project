package com.likelion.mutsasns.support;

import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;

import java.time.LocalDateTime;

public class PostFixture {
    private Integer id;
    private String title;
    private String body;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public PostFixture(User user){
        this.id = 1;
        this.title = "게시글 제목";
        this.body = "게시글 내용";
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    public Post createPost(){
        return Post.builder()
                .id(id)
                .title(title)
                .body(body)
                .user(user)
                .build();
    }
}
