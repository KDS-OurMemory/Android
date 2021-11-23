package com.skts.ourmemory.model.notice;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class DeleteNoticePostResult extends BasePostResult {
    @SerializedName("response")
    private NoticeDAO response;

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

    public NoticeDAO getResponse() {
        return response;
    }
}