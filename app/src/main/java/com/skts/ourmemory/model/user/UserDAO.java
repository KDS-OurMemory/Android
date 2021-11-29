package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDAO implements Serializable {
    @SerializedName("userId")
    private int userId;
    @SerializedName("snsType")
    private String snsType;
    @SerializedName("snsId")
    private String snsId;
    @SerializedName("pushToken")
    private String pushToken;
    @SerializedName("push")
    private boolean push;
    @SerializedName("name")
    private String name;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("solar")
    private boolean solar;
    @SerializedName("birthdayOpen")
    private boolean birthdayOpen;
    @SerializedName("role")
    private String role;
    @SerializedName("deviceOs")
    private String deviceOs;
    @SerializedName("privateRoomId")
    private int privateRoomId;
    @SerializedName("profileImageUrl")
    private String profileImageUrl;

    public int getUserId() {
        return userId;
    }

    public String getSnsType() {
        return snsType;
    }

    public String getSnsId() {
        return snsId;
    }

    public String getPushToken() {
        return pushToken;
    }

    public boolean isPush() {
        return push;
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

    public String getRole() {
        return role;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public int getPrivateRoomId() {
        return privateRoomId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
