package com.skts.ourmemory.model;

public class TestModel {
    String mUserType;
    String mUserNickname;
    String mUserBirthday;
    int mUserBirthdayOpen;

    public TestModel(String userType, String userNickname, String userBirthday, int userBirthdayOpen) {
        this.mUserType = userType;
        this.mUserNickname = userNickname;
        this.mUserBirthday = userBirthday;
        this.mUserBirthdayOpen = userBirthdayOpen;
    }
}
