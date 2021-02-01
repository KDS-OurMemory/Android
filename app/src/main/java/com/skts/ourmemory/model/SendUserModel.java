package com.skts.ourmemory.model;

public class SendUserModel {
    String mUserSnsId;
    String mUserName;
    String mUserBirthday;
    boolean mUserBirthdayType;
    boolean mUserBirthdayOpen;
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
