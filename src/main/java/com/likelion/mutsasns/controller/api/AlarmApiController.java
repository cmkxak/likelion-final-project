package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.alarm.AlarmResponse;
import com.likelion.mutsasns.service.AlarmService;
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

    @GetMapping
    public Response<Page<AlarmResponse>> findAlarms(@PageableDefault(size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return Response.success(alarmService.findAlarms(pageable));
    }
}
