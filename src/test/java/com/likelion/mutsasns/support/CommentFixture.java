package com.likelion.mutsasns.support;

import com.likelion.mutsasns.domain.entity.Comment;
import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;

public class CommentFixture {
    private Integer id;
    private String comment;
    private Post post;
    private User user;

    public CommentFixture(Integer id, String comment, Post post, User user) {
        this.id = id;
        this.comment = comment;
        this.post = post;
        this.user = user;
    }

    public Comment createComment(){
        return Comment.builder()
                .id(id)
                .comment(comment)
                .post(post)
                .user(user)
                .build();
    }
}
