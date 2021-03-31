package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        @SerializedName("id")
        String id;
        @SerializedName("name")
        String name;
        @SerializedName("birthday")
        String birthday;
        @SerializedName("isSolar")
        boolean isSolar;
        @SerializedName("isBirthdayOpen")
        boolean isBirthdayOpen;

        public String getId() {
            return id;
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
    }
}
