package com.likelion.mutsasns.controller;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.request.user.UserJoinRequest;
import com.likelion.mutsasns.domain.dto.response.user.UserJoinResponse;
import com.likelion.mutsasns.domain.dto.request.user.UserLoginRequest;
import com.likelion.mutsasns.domain.dto.response.user.UserLoginResponse;
import com.likelion.mutsasns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        UserJoinResponse userJoinResponse = userService.join(request);
        return Response.success(userJoinResponse);
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getUserName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
