package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("result")
    private int result;
    @SerializedName("joinTime")
    private String joinTime;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }
}
