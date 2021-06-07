package com.skts.ourmemory.model.notice;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class NoticePostResult extends BasePostResult {
    @SerializedName("response")
    private List<ResponseValue> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public List<ResponseValue> getResponse() {
        return response;
    }

    public static class ResponseValue {
        @SerializedName("noticeId")
        private int noticeId;
        @SerializedName("type")
        private String type;
        @SerializedName("value")
        private String value;
        @SerializedName("createDate")
        private String createDate;
    }
}