package com.likelion.mutsasns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.mutsasns.domain.dto.request.post.PostSaveRequest;
import com.likelion.mutsasns.domain.dto.response.post.PostResponse;
import com.likelion.mutsasns.domain.dto.response.post.PostSaveResponse;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostApiController.class)
class PostApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    PostSaveRequest saveRequest = PostSaveRequest.builder()
            .title("새로운 제목")
            .body("내용입니다.")
            .build();

    @Test
    @DisplayName("포스트 상세 조회 성공")
    @WithMockUser
    void find_one_post_success() throws Exception {
        PostResponse postResponse = PostResponse.builder()
                .id(1)
                .title("제목입니다.")
                .body("내용입니다.")
                .createdAt(LocalDateTime.now())
                .userName("root")
                .build();

        when(postService.findOne(any())).thenReturn(postResponse);

        mockMvc.perform(get("/api/v1/posts/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists());
    }

    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void create_post_success() throws Exception {
        when(postService.createPost(any(), any())).thenReturn(new PostSaveResponse("포스트 등록 완료", 1));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("포스트 작성 실패 - Bearer Token이 아닌 경우")
    @WithAnonymousUser
        //인증 되지 않은 상태
    void create_post_fail_not_bearer_token() throws Exception {
        when(postService.createPost(any(), any())).thenThrow(new AppException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 작성 실패 - JWT가 유효하지 않은 경우")
    @WithAnonymousUser
    void create_post_fail_invalid_token() throws Exception {
        when(postService.createPost(any(), any())).thenThrow(new AppException(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage()));

        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_TOKEN.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void update_post_success() throws Exception {
        when(postService.updatePost(any(), any(), any()))
                .thenReturn(new PostSaveResponse("포스트 수정 완료", 1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 수정 실패 - 인증 실패")
    @WithAnonymousUser
    void update_post_fail_authentication_fail() throws Exception {
        when(postService.updatePost(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 수정 실패 - 작성자 불일치")
    @WithMockUser
    void update_post_fail_not_same_user() throws Exception {
        when(postService.updatePost(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 수정 실패 - 데이터베이스 에러")
    @WithMockUser
    void update_post_fail_database_error() throws Exception {
        when(postService.updatePost(any(), any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 삭제 성공")
    @WithMockUser
    void delete_post_success() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 인증 실패")
    @WithAnonymousUser
    void delete_post_fail_authentication_fail() throws Exception {
        when(postService.deletePost(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 작성자 불일치")
    @WithMockUser
    void delete_post_fail_not_same_user() throws Exception {
        when(postService.deletePost(any(), any()))
                .thenThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("포스트 삭제 실패 - 데이터베이스 에러")
    @WithMockUser
    void delete_post_fail_database_error() throws Exception {
        when(postService.deletePost(any(), any()))
                .thenThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }
}