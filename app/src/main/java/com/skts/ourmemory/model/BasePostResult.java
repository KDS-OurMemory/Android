package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class BasePostResult {
    @SerializedName("resultcode")
    private String resultCode;
    @SerializedName("message")
    private String message;

    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
