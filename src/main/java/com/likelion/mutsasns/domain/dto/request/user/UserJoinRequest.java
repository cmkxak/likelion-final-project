package com.likelion.mutsasns.domain.dto.request.user;

import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.enumerate.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(String password) {
        return User.builder()
                .userName(userName)
                .password(password)
                .role(UserRole.USER)
                .build();
    }
}
