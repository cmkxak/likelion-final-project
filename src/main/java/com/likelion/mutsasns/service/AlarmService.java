package com.likelion.mutsasns.service;

import com.likelion.mutsasns.domain.dto.response.alarm.AlarmResponse;
import com.likelion.mutsasns.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Page<AlarmResponse> findAlarms(Pageable pageable) {
        return AlarmResponse.of(alarmRepository.findAll(pageable));
    }
}
