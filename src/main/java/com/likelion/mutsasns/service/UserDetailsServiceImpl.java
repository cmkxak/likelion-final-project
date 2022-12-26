package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.entity.UserDetailsImpl;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, username + "는 존재하지 않는 유저입니다."));
        if (user != null){
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            return userDetails;
        }
        return null;
    }
}
