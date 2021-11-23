package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.user.UserDAO;

public class UploadProfilePostResult extends BasePostResult {
    @SerializedName("response")
    private UserDAO response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
    }

    public UserDAO getResponse() {
        return response;
    }
}
