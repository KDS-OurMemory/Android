package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class RoomPostResult extends BasePostResult {
    @SerializedName("response")
    private List<RoomResponseValue> responseValueList;

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

    public List<RoomResponseValue> getResponseValueList() {
        return responseValueList;
    }
}
