package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attendance implements Serializable {
    @SerializedName("userId")
    private int userId;
    @SerializedName("status")
    private String status;

    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }
}