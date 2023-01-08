package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.alarm.AlarmResponse;
import com.likelion.mutsasns.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
@RestController
public class AlarmApiController {

    private final AlarmService alarmService;
    
    @ApiOperation(value = "알람 리스트 조회", notes = "게시글에 댓글, 좋아요가 눌렸을 경우 저장된 알림을 조회한다.")
    @GetMapping
    public Response<Page<AlarmResponse>> findAlarms(@PageableDefault(size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return Response.success(alarmService.findAlarms(pageable));
    }
}
