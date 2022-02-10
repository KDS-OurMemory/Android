package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class MyPagePostResult extends BasePostResult {
    @SerializedName("response")
    private UserDAO response;

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

    public UserDAO getResponse() {
        return response;
    }
}
