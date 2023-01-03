package com.likelion.mutsasns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.mutsasns.controller.api.UserApiController;
import com.likelion.mutsasns.domain.dto.request.user.UserJoinRequest;
import com.likelion.mutsasns.domain.dto.request.user.UserLoginRequest;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 가입 성공")
    @WithMockUser
    void join_success() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("test")
                .password("testpw")
                .build();

        User user = User.registerUser(userJoinRequest.getUserName(), userJoinRequest.getPassword());

        when(userService.join(any(),any())).thenReturn(user);

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 실패 -  userName이 중복인 경우")
    @WithMockUser
    void join_fail() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("root")
                .password("password")
                .build();

        when(userService.join(any(), any()))
                .thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, ErrorCode.DUPLICATED_USER_NAME.getMessage()));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getHttpStatus().value()));
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("root")
                .password("password")
                .build();

        when(userService.login(any(), any())).thenReturn("jwt");

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").exists())
                .andExpect(jsonPath("$.result.jwt").exists());
    }

    @Test
    @DisplayName("로그인 실패 - userName이 없는 경우")
    @WithMockUser
    void login_fail() throws Exception{
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("chulmin88")
                .password("1q2w3e4r")
                .build();

        when(userService.login(any(), any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,
                userLoginRequest.getUserName() + "는 존재하지 않는 유저입니다."));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @DisplayName("로그인 실패 - password 틀린 경우")
    @WithMockUser
    void login_fail2() throws Exception{
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("root")
                .password("1q2w3e4r!!!")
                .build();

        when(userService.login(any(), any())).thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, "잘못된 비밀번호 입니다."));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getHttpStatus().value()));
    }
}