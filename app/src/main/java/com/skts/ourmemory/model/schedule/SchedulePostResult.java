package com.skts.ourmemory.model.schedule;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.io.Serializable;
import java.util.List;

public class SchedulePostResult extends BasePostResult {
    @SerializedName("response")
    private List<ResponseValue> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public List<ResponseValue> getResponse() {
        return response;
    }

    public static class ResponseValue implements Serializable {
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
        @SerializedName("shareRooms")
        private List<ShareRoom> shareRooms;

        public ResponseValue(int memoryId, int writerId, String name, String contents, String place, String startDate, String endDate, String bgColor, String firstAlarm, String secondAlarm, String regDate, String modDate) {
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

        public void setRegDate(String regDate) {
            this.regDate = regDate;
        }
    }

    public static class ShareRoom implements Serializable {
        @SerializedName("roomId")
        private int roomId;
        @SerializedName("ownerId")
        private int ownerId;
        @SerializedName("name")
        private String name;

        public int getRoomId() {
            return roomId;
        }

        public int getOwnerId() {
            return ownerId;
        }

        public String getName() {
            return name;
        }
    }
}
