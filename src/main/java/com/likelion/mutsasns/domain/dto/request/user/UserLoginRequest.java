package com.likelion.mutsasns.domain.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserLoginRequest {
    private String userName;
    private String password;
}
