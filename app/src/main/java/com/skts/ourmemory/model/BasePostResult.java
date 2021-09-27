package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class BasePostResult {
    @SerializedName("resultCode")
    private String resultCode;
    @SerializedName("message")
    private String message;
    @SerializedName("responseDate")
    private String responseDate;

    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public String getResponseDate() {
        return responseDate;
    }
}
