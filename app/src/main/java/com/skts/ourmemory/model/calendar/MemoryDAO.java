package com.skts.ourmemory.model.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemoryDAO {
    @SerializedName("memoryId")
    private int memoryId;
    @SerializedName("writerId")
    private int writerId;
    @SerializedName("name")
    private String name;
    @SerializedName("contents")
    private String contents;
    @SerializedName("place")
    private String place;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("endDate")
    private String endDate;
    @SerializedName("bgColor")
    private String bgColor;
    @SerializedName("firstAlarm")
    private String firstAlarm;
    @SerializedName("secondAlarm")
    private String secondAlarm;
    @SerializedName("regDate")
    private String regDate;
    @SerializedName("modDate")
    private String modDate;
    @SerializedName("userAttendances")
    private List<Attendance> userAttendancesList;

    public int getMemoryId() {
        return memoryId;
    }

    public int getWriterId() {
        return writerId;
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

    public String getBgColor() {
        return bgColor;
    }

    public String getFirstAlarm() {
        return firstAlarm;
    }

    public String getSecondAlarm() {
        return secondAlarm;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getModDate() {
        return modDate;
    }

    public List<Attendance> getUserAttendancesList() {
        return userAttendancesList;
    }

    public static class Attendance {
        @SerializedName("userId")
        private int userId;
        @SerializedName("status")
        private String status;

        public int getUserId() {
            return userId;
        }

        public String getStatus() {
            return status;
        }
    }
}
