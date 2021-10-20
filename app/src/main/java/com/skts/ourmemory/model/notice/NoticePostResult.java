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

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
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
        @SerializedName("regDate")
        private String regDate;

        public int getNoticeId() {
            return noticeId;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public String getRegDate() {
            return regDate;
        }
    }
}