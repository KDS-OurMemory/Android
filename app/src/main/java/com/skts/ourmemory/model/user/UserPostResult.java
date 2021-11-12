package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.friend.FriendDAO;

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

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
    }

    public List<FriendDAO> getResponse() {
        return response;
    }
}
