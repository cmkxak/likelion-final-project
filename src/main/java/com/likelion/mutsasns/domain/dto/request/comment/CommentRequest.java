package com.likelion.mutsasns.domain.dto.request.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentRequest {

    @NotEmpty(message = "댓글은 한 글자 이상 입력 해야 합니다.")
    private String comment;
}
