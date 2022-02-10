package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class EachRoomPostResult extends BasePostResult {
    @SerializedName("response")
    private RoomResponseValue responseValue;

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

    public RoomResponseValue getResponseValue() {
        return responseValue;
    }
}
