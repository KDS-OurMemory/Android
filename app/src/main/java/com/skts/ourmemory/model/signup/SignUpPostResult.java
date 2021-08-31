package com.skts.ourmemory.model.signup;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

public class SignUpPostResult extends BasePostResult {
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

    public static class ResponseValue {
        @SerializedName("userId")
        private int userId;
        @SerializedName("privateRoomId")
        private int privateRoomId;
        @SerializedName("joinDate")
        private String joinDate;

        public int getUserId() {
            return userId;
        }

        public int getPrivateRoomId() {
            return privateRoomId;
        }

        public String getJoinDate() {
            return joinDate;
        }
    }
}
