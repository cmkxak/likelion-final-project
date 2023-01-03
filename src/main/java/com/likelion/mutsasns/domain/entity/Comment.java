package com.likelion.mutsasns.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id") //Comment 엔티티에 존재하는 post 필드를 어떤 이름으로 Comment 테이블에 나타날 지 컬럼명을 지정할지 나타내는 것
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Comment createComment(String comment, Post post, User user) {
        return Comment.builder()
                .comment(comment)
                .post(post)
                .user(user)
                .build();
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
}
