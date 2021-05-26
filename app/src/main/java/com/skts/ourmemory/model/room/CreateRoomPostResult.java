package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

public class CreateRoomPostResult extends BasePostResult {
    @SerializedName("response")
    private ResponseValue responseValue;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ResponseValue getResponseValue() {
        return responseValue;
    }

    @NotNull
    @Override
    public String toString() {
        return "LoginPostResult{" +
                "resultCode=" + super.getResultCode() +
                ", message='" + super.getMessage() + '\'' +
                ", roomId=" + responseValue.getRoomId() + '\'' +
                ", createDate='" + responseValue.getCreateDate() + '\'' +
                '}';
    }

    public static class ResponseValue {
        @SerializedName("roomId")
        private int roomId;
        @SerializedName("createDate")
        private String createDate;

        public int getRoomId() {
            return roomId;
        }

        public String getCreateDate() {
            return createDate;
        }
    }
}
