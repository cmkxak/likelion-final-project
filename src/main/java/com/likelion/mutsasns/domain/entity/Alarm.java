package com.likelion.mutsasns.domain.entity;

import com.likelion.mutsasns.enumerate.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Alarm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer targetId;

    private Integer fromUserId;

    private String text;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Alarm createAlarm(
            Integer targetId, Integer fromUserId,
            String text, AlarmType alarmType,
            User user) {

        return Alarm.builder()
                .targetId(targetId)
                .fromUserId(fromUserId)
                .text(text)
                .alarmType(alarmType)
                .user(user)
                .build();
    }
}
