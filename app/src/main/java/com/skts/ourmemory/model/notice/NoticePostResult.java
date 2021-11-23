package com.skts.ourmemory.model.notice;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class NoticePostResult extends BasePostResult {
    @SerializedName("response")
    private List<NoticeDAO> response;

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

    public List<NoticeDAO> getResponse() {
        return response;
    }
}