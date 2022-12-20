package com.likelion.mutsasns.domain;

import javax.persistence.*;

@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
