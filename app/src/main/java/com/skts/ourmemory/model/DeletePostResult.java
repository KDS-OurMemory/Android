
package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class DeletePostResult extends BasePostResult {
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
        @SerializedName("requestDate")
        private String deleteDate;

        public String getDeleteDate() {
            return deleteDate;
        }
    }
}
