package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class PatchPostResult extends BasePostResult {
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

    public ResponseValue getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "PatchPostResult{" +
                "resultCode=" + super.getResultCode() +
                ", message='" + super.getMessage() + '\'' +
                ", patchDate=" + response.getPatchDate() + '\'' +
                '}';
    }

    public static class ResponseValue {
        @SerializedName("patchDate")
        private String patchDate;

        public String getPatchDate() {
            return patchDate;
        }
    }
}
