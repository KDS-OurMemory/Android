package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.user.UserDAO;

import java.io.Serializable;
import java.util.List;

public class RoomPostResult extends BasePostResult {
    @SerializedName("response")
    private List<RoomResponseValue> responseValueList;

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

    public List<RoomResponseValue> getResponseValueList() {
        return responseValueList;
    }
}
