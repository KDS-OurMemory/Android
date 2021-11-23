package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.user.UserDAO;

public class LoginPostResult extends BasePostResult {
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
