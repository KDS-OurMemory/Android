package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class UserDAO {
    @SerializedName("userId")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("isSolar")
    private boolean isSolar;
    @SerializedName("isBirthdayOpen")
    private boolean isBirthdayOpen;

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean isSolar() {
        return isSolar;
    }

    public boolean isBirthdayOpen() {
        return isBirthdayOpen;
    }
}
