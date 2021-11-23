package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FriendDAO implements Serializable {
    @SerializedName("friendId")
    private int friendId;
    @SerializedName("name")
    private String name;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("solar")
    private boolean solar;
    @SerializedName("birthdayOpen")
    private boolean birthdayOpen;
    @SerializedName("friendStatus")
    private String friendStatus;
    @SerializedName("profileImageUrl")
    private String profileImageUrl;

    public int getFriendId() {
        return friendId;
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

    public String getFriendStatus() {
        return friendStatus;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
