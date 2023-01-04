package com.likelion.mutsasns.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.mutsasns.domain.dto.request.comment.CommentRequest;
import com.likelion.mutsasns.domain.dto.response.comment.CommentDeleteResponse;
import com.likelion.mutsasns.domain.dto.response.comment.CommentResponse;
import com.likelion.mutsasns.domain.entity.Post;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.enumerate.UserRole;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.service.CommentService;
import com.likelion.mutsasns.support.CommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//웹에서 사용되는 요청과 응답에 대한 테스트를 수행할 수 있음
@WebMvcTest(CommentApiController.class)
class CommentApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Autowired
    ObjectMapper objectMapper;

    User user = new User(1, "gcm46", "1q2w3e4r", UserRole.USER);
    Post post = new Post(1, "글 제목", "글 내용", user);

    CommentRequest commentRequest = new CommentRequest("댓글 작성");
    CommentResponse commentResponse = CommentResponse.builder()
            .id(1)
            .userName(user.getUserName())
            .comment("댓글 작성 완료.")
            .createdAt(LocalDateTime.now())
            .postId(post.getId())
            .build();

    @Test
    @DisplayName("댓글 목록 조회 성공")
    @WithMockUser
    void comment_list_success() throws Exception {
        CommentFixture commentFixture = new CommentFixture(1,"댓글", post, user);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        given(commentService.findAll(any(), any(Pageable.class)))
                .willReturn(CommentResponse.of(new PageImpl<>(List.of(commentFixture.createComment()))));

        mockMvc.perform(get("/api/v1/posts/1/comments")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(commentService).findAll(eq(1), eq(pageable));
    }

    @Test
    @DisplayName("댓글 작성 성공")
    @WithMockUser
    public void comment_success() throws Exception {
        given(commentService.createComment(any(), any(), any()))
                .willReturn(commentResponse);

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.postId").exists());
    }

    @Test
    @DisplayName("댓글 작성 실패 - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void comment_fail() throws Exception {
        given(commentService.createComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 작성 실패 - 게시물이 존재하지 않는 경우")
    @WithMockUser
    void comment_fail2() throws Exception {
        given(commentService.createComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(post("/api/v1/posts/1/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 수정 성공")
    @WithMockUser
    void comment_update_success() throws Exception {
        given(commentService.updateComment(any(), any(), any(), any()))
                .willReturn(commentResponse);

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 인증 실패")
    void comment_update_fail() throws Exception {
        given(commentService.updateComment(any(), any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자 불일치")
    @WithMockUser
    void comment_update_fail2() throws Exception {
        given(commentService.updateComment(any(), any(), any(), any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자 불일치")
    @WithMockUser
    void comment_update_fail3() throws Exception {
        given(commentService.updateComment(any(), any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 데이터베이스 에러")
    @WithMockUser
    void comment_update_fail4() throws Exception {
        given(commentService.updateComment(any(), any(), any(), any()))
                .willThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(put("/api/v1/posts/1/comments/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    @WithMockUser
    void comment_delete_success() throws Exception {
        given(commentService.deleteComment(any(), any(), any()))
                .willReturn(new CommentDeleteResponse("댓글 삭제 완료.", 1));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 인증 실패")
    void comment_delete_fail() throws Exception {
        given(commentService.deleteComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 포스트가 없는 경우")
    @WithMockUser
    void comment_delete_fail2() throws Exception {
        given(commentService.deleteComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.POST_NOT_FOUND.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 데이터베이스 에러")
    @WithMockUser
    void comment_delete_fail3() throws Exception {
        given(commentService.deleteComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.DATABASE_ERROR, ErrorCode.DATABASE_ERROR.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DATABASE_ERROR.getHttpStatus().value()));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 작성자 불일치")
    @WithMockUser
    void comment_delete_fail4() throws Exception {
        given(commentService.deleteComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(delete("/api/v1/posts/1/comments/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }
}