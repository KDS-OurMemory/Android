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

    @NotNull
    @Override
    public String toString() {
        return "SignUpPostResult{" +
                "resultCode=" + super.getResultCode() +
                ", message='" + super.getMessage() + '\'' +
                ", userId=" + response.getUserId() + '\'' +
                ", joinDate='" + response.getJoinDate() + '\'' +
                '}';
    }

    public static class ResponseValue {
        @SerializedName("userId")
        private int userId;
        @SerializedName("joinDate")
        private String joinDate;

        public int getUserId() {
            return userId;
        }

        public String getJoinDate() {
            return joinDate;
        }
    }
}
