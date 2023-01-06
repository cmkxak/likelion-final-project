package com.likelion.mutsasns.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Post createPost(String title, String body, User user){
        return Post.builder()
                .title(title)
                .body(body)
                .user(user)
                .build();
    }

    public void updatePost(String title, String body){
        this.title = title;
        this.body = body;
    }

    public void deletePost(){
        this.setDeletedAt(LocalDateTime.now());
    }
}
