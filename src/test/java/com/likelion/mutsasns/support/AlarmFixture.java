package com.likelion.mutsasns.support;

import com.likelion.mutsasns.domain.entity.Alarm;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.enumerate.AlarmType;

public class AlarmFixture {
    private final Integer id;
    private final Integer targetId;
    private final Integer fromUserId;
    private final String text;
    private final AlarmType alarmType;
    private final User user;

    public AlarmFixture(User user){
        this.id = 1;
        this.targetId = 1;
        this.fromUserId = 2;
        this.text= AlarmType.NEW_COMMENT_ON_POST.getMessage();
        this.alarmType = AlarmType.NEW_COMMENT_ON_POST;
        this.user = user;
    }

    public Alarm createAlarm(){
        return Alarm.builder()
                .id(id)
                .targetId(targetId)
                .fromUserId(fromUserId)
                .text(text)
                .alarmType(alarmType)
                .user(user)
                .build();
    }
}
