package com.skts.ourmemory.model.schedule;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EditSchedulePost {
    @SerializedName("name")
    private String name;                // 일정 제목
    @SerializedName("contents")
    private String contents;            // 일정 내용
    @SerializedName("place")
    private String place;               // 장소
    @SerializedName("startDate")
    private String startDate;           // 시작일
    @SerializedName("endDate")
    private String endDate;             // 종료일
    @SerializedName("firstAlarm")
    private String firstAlarm;          // 첫 번째 알람
    @SerializedName("secondAlarm")
    private String secondAlarm;         // 두 번째 알람
    @SerializedName("bgColor")
    private String bgColor;             // 메모지 색깔

    public EditSchedulePost() {
    }

    public EditSchedulePost(int userId, String name, List<Integer> members, String contents, String place, String startDate, String endDate,
                            String firstAlarm, String secondAlarm, String bgColor, List<Integer> sharedRooms) {
        this.name = name;
        this.contents = contents;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstAlarm = firstAlarm;
        this.secondAlarm = secondAlarm;
        this.bgColor = bgColor;
    }

    public String getName() {
        return name;
    }

    public String getContents() {
        return contents;
    }

    public String getPlace() {
        return place;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getFirstAlarm() {
        return firstAlarm;
    }

    public String getSecondAlarm() {
        return secondAlarm;
    }

    public String getBgColor() {
        return bgColor;
    }
}
