package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class UserPostResult extends BasePostResult {
    @SerializedName("response")
    private List<FriendDAO> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public List<FriendDAO> getResponse() {
        return response;
    }
}
