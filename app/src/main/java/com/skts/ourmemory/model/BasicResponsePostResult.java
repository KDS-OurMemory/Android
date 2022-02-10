package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class BasicResponsePostResult extends BasePostResult {
    @SerializedName("response")
    private ResponseValue response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getResultMessage() {
        return super.getResultMessage();
    }

    @Override
    public String getDetailMessage() {
        return super.getDetailMessage();
    }

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
    }

    public ResponseValue getResponse() {
        return response;
    }

    public static class ResponseValue {
    }
}
