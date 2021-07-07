package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.UserDAO;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserPostResult extends BasePostResult {
    @SerializedName("response")
    private List<UserDAO> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public List<UserDAO> getResponse() {
        return response;
    }
}
