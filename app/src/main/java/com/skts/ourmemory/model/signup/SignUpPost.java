package com.skts.ourmemory.model.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpPost {
    @SerializedName("snsId")
    String mSnsId;
    @SerializedName("name")
    String mName;
    @SerializedName("birthday")
    String mBirthday;
    @SerializedName("solar")
    boolean mSolar;
    @SerializedName("birthdayOpen")
    boolean mBirthdayOpen;
    @SerializedName("snsType")
    int mSnsType;
    @SerializedName("pushToken")
    String mPushToken;
    @SerializedName("deviceOs")
    String mDeviceOs;

    public SignUpPost(String snsId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int snsType, String pushToken, String deviceOs) {
        this.mSnsId = snsId;
        this.mName = userName;
        this.mBirthday = userBirthday;
        this.mSolar = userBirthdayType;
        this.mBirthdayOpen = userBirthdayOpen;
        this.mSnsType = snsType;
        this.mPushToken = pushToken;
        this.mDeviceOs = deviceOs;
    }
}
