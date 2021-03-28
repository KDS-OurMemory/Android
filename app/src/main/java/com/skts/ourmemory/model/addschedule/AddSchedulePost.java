package com.skts.ourmemory.model.addschedule;

import com.google.gson.annotations.SerializedName;

public class AddSchedulePost {
    @SerializedName("snsId")
    private String snsId;           // snsid
    @SerializedName("name")
    private String name;            // 일정 제목
    @SerializedName("contents")
    private String contents;        // 일정 내용
    @SerializedName("place")
    private String place;           // 장소
    @SerializedName("startDate")
    private String startDate;       // 시작일
    @SerializedName("endDate")
    private String endDate;         // 종료일
    @SerializedName("firstAlarm")
    private String firstAlarm;      // 첫 번째 알람
    @SerializedName("secondAlarm")
    private String secondAlarm;     // 두 번째 알람
    @SerializedName("bgColor")
    private String bgColor;         // 메모지 색깔

    public AddSchedulePost(String snsId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor) {
        this.snsId = snsId;
        this.name = name;
        this.contents = contents;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstAlarm = firstAlarm;
        this.secondAlarm = secondAlarm;
        this.bgColor = bgColor;
    }
}
