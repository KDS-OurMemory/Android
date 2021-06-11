
package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

public class RequestFriendPostResult extends BasePostResult {
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

    @NotNull
    @Override
    public String toString() {
        try {
            return "RequestFriendPostResult{" +
                    "responseValue=" + responseValue +
                    "addDate=" + responseValue.requestDate +
                    '}';
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class ResponseValue {
        @SerializedName("requestDate")
        private String requestDate;

        public String getRequestDate() {
            return requestDate;
        }
    }
}
