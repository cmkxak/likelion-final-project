package com.likelion.mutsasns.domain.dto.response.user;

import com.likelion.mutsasns.enumerate.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserModifyRoleResponse {
    private String message;
    private UserRole role;
}
