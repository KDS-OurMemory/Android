package com.skts.ourmemory.model.login;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class LoginPostResult {
    @SerializedName("resultCode")
    private int resultCode;

    @NotNull
    @Override
    public String toString() {
        return "LoginPostResult{" +
                "resultCode=" + resultCode +
                '}';
    }
}
