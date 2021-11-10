package com.skts.ourmemory.model.memory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MemoryDAO implements Serializable {
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

    public MemoryDAO() {
    }

    public MemoryDAO(int memoryId, int writerId, String name, String contents, String place, String startDate, String endDate, String bgColor, String firstAlarm, String secondAlarm, String regDate, String modDate, List<Attendance> userAttendancesList) {
        this.memoryId = memoryId;
        this.writerId = writerId;
        this.name = name;
        this.contents = contents;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bgColor = bgColor;
        this.firstAlarm = firstAlarm;
        this.secondAlarm = secondAlarm;
        this.regDate = regDate;
        this.modDate = modDate;
        this.userAttendancesList = userAttendancesList;
    }

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

    public void setRegDate(String regDate) {
        this.regDate = regDate;
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
