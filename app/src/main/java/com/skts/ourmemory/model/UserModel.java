package com.skts.ourmemory.model;

import java.io.Serializable;

public class UserModel implements IUser, Serializable {
    String mName;
    String mEmail;
    String mGender;
    String mBirthday;
    String mMobile;

    // 카카오용


    // 구글용
    public UserModel(String name, String email) {
        this.mName = name;
        this.mEmail = email;
    }

    // 네이버용
    public UserModel(String name, String email, String gender, String birthday, String mobile) {
        this.mName = name;
        this.mEmail = email;
        this.mGender = gender;
        this.mBirthday = birthday;
        this.mMobile = mobile;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getEmail() {
        return mEmail;
    }

    @Override
    public String getGender() {
        return mGender;
    }

    @Override
    public String getBirthday() {
        return mBirthday;
    }

    @Override
    public String getMobile() {
        return mMobile;
    }
}
