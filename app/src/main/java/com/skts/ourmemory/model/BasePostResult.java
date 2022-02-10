package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class BasePostResult {
    @SerializedName("resultCode")
    private String resultCode;
    @SerializedName("resultMessage")
    private String resultMessage;
    @SerializedName("detailMessage")
    private String detailMessage;
    @SerializedName("responseDate")
    private String responseDate;

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getResponseDate() {
        return responseDate;
    }
}
