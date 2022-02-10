
package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class SharePostResult extends BasePostResult {
    @SerializedName("response")
    private ResponseValue responseValue;

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

    public ResponseValue getResponseValue() {
        return responseValue;
    }

    public static class ResponseValue {
        @SerializedName("shareDate")
        private String shareDate;

        public String getShareDate() {
            return shareDate;
        }
    }
}
