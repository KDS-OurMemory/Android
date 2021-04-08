package com.skts.ourmemory.model.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpPostResult {
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
        return "SignUpPostResult{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }

    public class ResponseValue {
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
