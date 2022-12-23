package com.likelion.mutsasns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.mutsasns.domain.dto.request.user.UserJoinRequest;
import com.likelion.mutsasns.domain.dto.request.user.UserLoginRequest;
import com.likelion.mutsasns.domain.dto.response.user.UserLoginResponse;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.service.UserService;
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

@WebMvcTest
class UserApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void 회원가입_성공() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("testId11")
                .password("1q2w3e4r")
                .build();

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 회원가입_실패() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("chulmin1")
                .password("1q2w3e4r1")
                .build();

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void 로그인_성공() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("chulmin1")
                .password("1q2w3e4r")
                .build();

        when(userService.login(any())).thenReturn(new UserLoginResponse("token"));

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
    @WithMockUser
    void 로그인_실패_userName이_없는_경우() throws Exception{
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("chulmin88")
                .password("1q2w3e4r")
                .build();

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USERNAME_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @WithMockUser
    void 로그인_실패_입력한_password가_틀린_경우() throws Exception{
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("chulmin1")
                .password("1q2w3e4r!!!")
                .build();

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getHttpStatus().value()));
    }
}