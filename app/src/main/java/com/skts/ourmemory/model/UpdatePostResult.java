
package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class UpdatePostResult extends BasePostResult {
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
        @SerializedName("updateDate")
        private String updateDate;

        public String getUpdateDate() {
            return updateDate;
        }
    }
}
