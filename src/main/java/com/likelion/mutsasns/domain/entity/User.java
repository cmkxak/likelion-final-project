package com.likelion.mutsasns.domain.entity;

import com.likelion.mutsasns.enumerate.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Integer id;

    @Column(unique = true)
    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public static User registerUser(String userName, String encPassword) {
        return User.builder()
                .userName(userName)
                .password(encPassword)
                .role(UserRole.USER)
                .build();
    }

    public void changeRole(UserRole role){
        this.role = role;
    }
}
