
package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

public class AddFriendPostResult extends BasePostResult {
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
            return "AddFriendPostResult{" +
                    "responseValue=" + responseValue +
                    "addDate=" + responseValue.addDate +
                    '}';
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class ResponseValue {
        @SerializedName("addDate")
        private String addDate;

        public String getAddDate() {
            return addDate;
        }
    }
}
