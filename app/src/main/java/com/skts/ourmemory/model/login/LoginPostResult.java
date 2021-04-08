package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginPostResult {
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
        return "LoginPostResult{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }

    public class ResponseValue {
        @SerializedName("userId")
        private int userId;
        @SerializedName("name")
        private String name;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("isSolar")
        private boolean isSolar;
        @SerializedName("isBirthdayOpen")
        private boolean isBirthdayOpen;
        @SerializedName("pushToken")
        private String pushToken;

        public int getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getBirthday() {
            return birthday;
        }

        public boolean isSolar() {
            return isSolar;
        }

        public boolean isBirthdayOpen() {
            return isBirthdayOpen;
        }

        public String getPushToken() {
            return pushToken;
        }
    }
}
