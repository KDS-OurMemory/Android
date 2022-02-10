package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class FriendPostResult extends BasePostResult {
    @SerializedName("response")
    private List<FriendDAO> response;

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

    public List<FriendDAO> getResponse() {
        return response;
    }
}
