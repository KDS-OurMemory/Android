package com.skts.ourmemory.model.schedule;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.UserDAO;

import java.io.Serializable;
import java.util.List;

public class AddSchedulePostResult extends BasePostResult implements Serializable {
    @SerializedName("response")
    private ResponseValue response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ResponseValue getResponse() {
        return response;
    }

    public static class ResponseValue implements Serializable {
        @SerializedName("mainRoomId")
        private int mainRoomId;
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
        @SerializedName("members")
        private List<UserDAO> members;

        public int getMainRoomId() {
            return mainRoomId;
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

        public String getModDate() {
            return modDate;
        }

        public List<UserDAO> getMembers() {
            return members;
        }
    }
}
