package com.skts.ourmemory.model.schedule;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.memory.MemoryDAO;

import java.util.List;

public class SchedulePostResult extends BasePostResult {
    @SerializedName("response")
    private List<MemoryDAO> response;

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

    public List<MemoryDAO> getResponse() {
        return response;
    }
}
