package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FriendDAO implements Serializable {
    @SerializedName("userId")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("solar")
    private boolean solar;
    @SerializedName("birthdayOpen")
    private boolean birthdayOpen;
    @SerializedName("profileImageUrl")
    private String profileImageUrl;
    @SerializedName("friendStatus")
    private String friendStatus;

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
        return solar;
    }

    public boolean isBirthdayOpen() {
        return birthdayOpen;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getFriendStatus() {
        return friendStatus;
    }
}
