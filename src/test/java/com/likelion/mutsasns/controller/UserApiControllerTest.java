package com.likelion.mutsasns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.mutsasns.domain.dto.user.UserJoinRequest;
import com.likelion.mutsasns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
}