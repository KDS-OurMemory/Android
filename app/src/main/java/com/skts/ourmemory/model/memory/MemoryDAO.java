package com.skts.ourmemory.model.memory;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.room.Attendance;
import com.skts.ourmemory.model.room.ShareRoom;

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
    @SerializedName("addedRoomId")
    private int addedRoomId;
    @SerializedName("shareRooms")
    private List<ShareRoom> shareRooms;
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

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getModDate() {
        return modDate;
    }

    public int getAddedRoomId() {
        return addedRoomId;
    }

    public List<ShareRoom> getShareRooms() {
        return shareRooms;
    }

    public List<Attendance> getUserAttendancesList() {
        return userAttendancesList;
    }
}
