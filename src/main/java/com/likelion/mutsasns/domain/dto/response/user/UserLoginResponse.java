package com.likelion.mutsasns.domain.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {
    private String jwt;
}
