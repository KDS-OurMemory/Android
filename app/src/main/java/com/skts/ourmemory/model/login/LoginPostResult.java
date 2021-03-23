package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;

public class LoginPostResult {
    @SerializedName("resultCode")
    private int resultCode;

    @Override
    public String toString() {
        return "LoginPostResult{" +
                "resultCode=" + resultCode +
                '}';
    }
}
