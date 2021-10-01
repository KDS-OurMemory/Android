package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDAO implements Serializable {
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

    public UserDAO(int userId, String name, String birthday, boolean solar, boolean birthdayOpen, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.birthday = birthday;
        this.solar = solar;
        this.birthdayOpen = birthdayOpen;
        this.profileImageUrl = profileImageUrl;
    }
}
