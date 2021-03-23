package com.skts.ourmemory.model.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpPostResult {
    @SerializedName("resultCode")
    private int resultCode;
    @SerializedName("joinTime")
    private String joinTime;

    public int getResult() {
        return resultCode;
    }

    public void setResult(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    @Override
    public String toString() {
        return "ReceiveUserModel{" + "resultCode=" + resultCode + ", joinTime='" + joinTime + '\'' + '}';
    }
}
