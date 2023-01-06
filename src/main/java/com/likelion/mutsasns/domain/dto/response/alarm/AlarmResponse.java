package com.likelion.mutsasns.domain.dto.response.alarm;

import com.likelion.mutsasns.domain.entity.Alarm;
import com.likelion.mutsasns.enumerate.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlarmResponse {
    private Integer id;
    private AlarmType alarmType;
    private Integer fromUserId;
    private Integer targetId;
    private String text;
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> of(Page<Alarm> alarms) {
        return alarms.map(alarm -> AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build());
    }
}
