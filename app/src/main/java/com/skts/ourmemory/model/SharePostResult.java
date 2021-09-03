
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
    public String getMessage() {
        return super.getMessage();
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
