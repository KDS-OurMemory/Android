package com.skts.ourmemory.model.addschedule;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

public class AddSchedulePostResult extends BasePostResult {
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

    @NotNull
    @Override
    public String toString() {
        return "AddSchedulePostResult{" +
                "resultCode=" + super.getResultCode() +
                ", message='" + super.getMessage() + '\'' +
                ", memoryId=" + response.getMemoryId() + '\'' +
                ", roomId='" + response.getRoomId() + '\'' +
                ", addDate='" + response.getAddDate() + '\'' +
                '}';
    }

    public static class ResponseValue {
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
