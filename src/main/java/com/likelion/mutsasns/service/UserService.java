package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.User;
import com.likelion.mutsasns.domain.dto.user.UserJoinRequest;
import com.likelion.mutsasns.domain.dto.user.UserJoinResponse;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.exception.UserNotFoundException;
import com.likelion.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserJoinResponse join(UserJoinRequest request) {
        validateDuplicateUser(request);
        String password = passwordEncoder.encode(request.getPassword());
        User savedUser = userRepository.save(request.toEntity(passwordEncoder.encode(password)));
        return new UserJoinResponse(savedUser.getId(), savedUser.getUserName());
    }

    private void validateDuplicateUser(UserJoinRequest request) {
        userRepository.findByUserName(request.getUserName()).ifPresent(user -> {
            throw new UserNotFoundException(ErrorCode.DUPLICATED_USER_NAME, user.getUserName());
        });
    }
}
