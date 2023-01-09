package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.response.alarm.AlarmResponse;
import com.likelion.mutsasns.exception.AppException;
import com.likelion.mutsasns.exception.ErrorCode;
import com.likelion.mutsasns.service.AlarmService;
import com.likelion.mutsasns.support.AlarmFixture;
import com.likelion.mutsasns.support.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmApiController.class)
class AlarmApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlarmService alarmService;

    @Test
    @DisplayName("알람 목록 조회 성공")
    @WithMockUser
    void alarm_list_success() throws Exception {
        UserFixture userFixture = new UserFixture();
        AlarmFixture alarmFixture = new AlarmFixture(userFixture.createUser());
        given(alarmService.findAlarms(any(Pageable.class)))
                .willReturn(AlarmResponse.of(new PageImpl<>(List.of(alarmFixture.createAlarm()))));

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알람 목록 조회 실패 - 로그인 하지 않은 경우")
    void alarm_list_fail() throws Exception {
        given(alarmService.findAlarms(any(Pageable.class)))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }
}