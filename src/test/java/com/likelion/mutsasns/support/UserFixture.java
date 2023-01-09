package com.likelion.mutsasns.support;

import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.enumerate.UserRole;

public class UserFixture {
    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;

    public UserFixture(){
        this.id = 1;
        this.userName = "toby";
        this.password = "1q2w3e4r";
        this.userRole = UserRole.USER;
    }

    public User createUser(){
        return User.builder()
                .id(id)
                .userName(userName)
                .password(password)
                .role(userRole)
                .build();
    }

}
