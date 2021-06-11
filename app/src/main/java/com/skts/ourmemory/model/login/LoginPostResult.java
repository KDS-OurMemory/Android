package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

public class LoginPostResult extends BasePostResult {
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
        try {
            return "LoginPostResult{" +
                    "resultCode=" + super.getResultCode() +
                    ", message='" + super.getMessage() + '\'' +
                    ", userId=" + response.getUserId() + '\'' +
                    ", name='" + response.getName() + '\'' +
                    ", birthday='" + response.getBirthday() + '\'' +
                    ", isSolar=" + response.isSolar() + '\'' +
                    ", isBirthdayOpen=" + response.isBirthdayOpen() + '\'' +
                    ", pushToken='" + response.getPushToken() + '\'' +
                    ", push='" + response.isPush() + '\'' +
                    '}';
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class ResponseValue {
        @SerializedName("userId")
        private int userId;
        @SerializedName("name")
        private String name;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("solar")
        private boolean solar;
        @SerializedName("birthdayOpen")
        private boolean birthdayOpen;
        @SerializedName("pushToken")
        private String pushToken;
        @SerializedName("push")
        private boolean push;

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
            return solar;
        }

        public boolean isBirthdayOpen() {
            return birthdayOpen;
        }

        public String getPushToken() {
            return pushToken;
        }

        public boolean isPush() {
            return push;
        }
    }
}
