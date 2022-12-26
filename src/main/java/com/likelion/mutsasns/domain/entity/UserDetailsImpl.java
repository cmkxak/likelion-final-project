package com.likelion.mutsasns.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//유저의 정보를 가져오는 UserDetails 인터페이스를 상속하는 클래스이다.
//Authentication을 담고 있으며, user.getRole().getValue()를 통해 사용자의 권한(Authorities)를 부여해 가져올 수 있다.
public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> user.getRole().getValue());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    //계정이 만료되었는지 리턴하는 메서드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨 있는지 리턴하는 메서드
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료됐는지 리턴하는 메서드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화돼 있는지 리턴하는 메서드
    @Override
    public boolean isEnabled() {
        return true;
    }
}
