package com.likelion.mutsasns.domain.dto.request.user;

import com.likelion.mutsasns.enumerate.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserChangeRoleRequest {
    private UserRole role;
}
