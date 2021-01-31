package com.skts.ourmemory.model;

import java.io.Serializable;

public class UserModel implements IUser, Serializable {
    String mId;             // SNS ID
    String mName;           // 다른 사람에게 보여줄 이름
    String mBirthday;       // 생일
    int mBirthdayType;      // 생일타입(양/음력)
    boolean mBirthdayOpen;  // 생일 공개 여부
    int mLoginType;         // 로그인 유형
    String mProfileUrl;     // 프로필 사진
    String mJoinDate;       // 가입 날짜

    // 구글용
    public UserModel(String id, String name, int loginType) {
        this.mId = id;
        this.mName = name;
        this.mLoginType = loginType;
    }

    // 카카오/네이버용
    public UserModel(String id, String name, String birthday, int loginType) {
        this.mId = id;
        this.mName = name;
        this.mBirthday = birthday;
        this.mLoginType = loginType;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getBirthday() {
        return mBirthday;
    }

    @Override
    public int getBirthdayType() {
        return mBirthdayType;
    }

    @Override
    public boolean getBirthdayOpen() {
        return mBirthdayOpen;
    }

    @Override
    public int getLoginType() {
        return mLoginType;
    }

    @Override
    public String getProfileUrl() {
        return mProfileUrl;
    }

    @Override
    public String getJoinDate() {
        return mJoinDate;
    }
}
