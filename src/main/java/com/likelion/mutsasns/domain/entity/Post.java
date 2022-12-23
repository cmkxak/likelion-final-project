package com.likelion.mutsasns.domain.entity;

import com.likelion.mutsasns.domain.dto.response.post.PostResponse;
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
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String body;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PostResponse toResponse() {
        return PostResponse.builder()
                .id(id)
                .title(title)
                .body(body)
                .userName(this.user.getUserName())
                .createdAt(super.getCreatedAt())
                .lastModifiedAt(super.getLastModifiedAt())
                .build();
    }
}
