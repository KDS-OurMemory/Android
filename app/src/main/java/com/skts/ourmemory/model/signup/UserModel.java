package com.skts.ourmemory.model.signup;

public class UserModel implements IUser {
    String mId;             // SNS ID
    String mName;           // 다른 사람에게 보여줄 이름
    String mBirthday;       // 생일
    boolean mBirthdayType;  // 생일타입(양/음력)
    boolean mBirthdayOpen;  // 생일 공개 여부
    int mSnsType;         // 로그인 유형
    String mProfileUrl;     // 프로필 사진
    String mSignUpDate;     // 가입 날짜

    public UserModel(String id, String name, String birthday, boolean birthdayType, boolean birthdayOpen, int snsType, String signUpDate) {
        this.mId = id;
        this.mName = name;
        this.mBirthday = birthday;
        this.mBirthdayType = birthdayType;
        this.mBirthdayOpen = birthdayOpen;
        this.mSnsType = snsType;
        this.mSignUpDate = signUpDate;
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
    public boolean getBirthdayType() {
        return mBirthdayType;
    }

    @Override
    public boolean getBirthdayOpen() {
        return mBirthdayOpen;
    }

    @Override
    public int getSnsType() {
        return mSnsType;
    }

    @Override
    public String getProfileUrl() {
        return mProfileUrl;
    }

    @Override
    public String getSignUpDate() {
        return mSignUpDate;
    }
}
