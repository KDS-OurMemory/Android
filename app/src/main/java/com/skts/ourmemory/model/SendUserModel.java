package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

public class SendUserModel {
    @SerializedName("snsId")
    String mUserSnsId;
    @SerializedName("name")
    String mUserName;
    @SerializedName("birthday")
    String mUserBirthday;
    @SerializedName("isSolar")
    boolean mUserBirthdayType;
    @SerializedName("isBirthdayOpen")
    boolean mUserBirthdayOpen;
    @SerializedName("snsType")
    int mLoginType;

    public SendUserModel(String userSnsId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int loginType) {
        this.mUserSnsId = userSnsId;
        this.mUserName = userName;
        this.mUserBirthday = userBirthday;
        this.mUserBirthdayType = userBirthdayType;
        this.mUserBirthdayOpen = userBirthdayOpen;
        this.mLoginType = loginType;
    }
}
