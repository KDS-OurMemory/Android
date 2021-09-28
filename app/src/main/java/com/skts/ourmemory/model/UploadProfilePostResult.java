package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.calendar.MemoryDAO;
import com.skts.ourmemory.model.user.UserDAO;

import java.util.List;

public class UploadProfilePostResult extends BasePostResult {
    @SerializedName("response")
    private ResponseValue response;

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

    public ResponseValue getResponse() {
        return response;
    }

    public static class ResponseValue {
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }
    }
}
