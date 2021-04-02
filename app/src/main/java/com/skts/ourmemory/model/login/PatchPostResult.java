package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;

public class PatchPostResult {
    @SerializedName("resultcode")
    private String resultCode;
    @SerializedName("message")
    private String message;
    @SerializedName("response")
    private ResponseValue response;

    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public ResponseValue getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "PatchPostResult{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }

    public class ResponseValue {
        @SerializedName("patchDate")
        private String patchDate;

        public String getPatchDate() {
            return patchDate;
        }
    }
}
