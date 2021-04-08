package com.skts.ourmemory.model.addschedule;

import com.google.gson.annotations.SerializedName;

public class AddSchedulePostResult {
    @SerializedName("resultcode")
    private String resultCode;
    @SerializedName("message")
    private String message;
    @SerializedName("response")
    private ResponseValue response;

    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public ResponseValue getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "AddSchedulePostResult{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }

    public class ResponseValue {
        @SerializedName("memoryId")
        private int memoryId;
        @SerializedName("roomId")
        private int roomId;
        @SerializedName("addDate")
        private String addDate;

        public int getMemoryId() {
            return memoryId;
        }

        public int getRoomId() {
            return roomId;
        }

        public String getAddDate() {
            return addDate;
        }
    }
}
