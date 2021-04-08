package com.skts.ourmemory.model.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpPost {
    @SerializedName("snsId")
    String mSnsId;
    @SerializedName("name")
    String mUserName;
    @SerializedName("birthday")
    String mUserBirthday;
    @SerializedName("isSolar")
    boolean mUserBirthdayType;
    @SerializedName("isBirthdayOpen")
    boolean mUserBirthdayOpen;
    @SerializedName("snsType")
    int mSnsType;
    @SerializedName("pushToken")
    String mPushToken;

    public SignUpPost(String snsId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int snsType, String pushToken) {
        this.mSnsId = snsId;
        this.mUserName = userName;
        this.mUserBirthday = userBirthday;
        this.mUserBirthdayType = userBirthdayType;
        this.mUserBirthdayOpen = userBirthdayOpen;
        this.mSnsType = snsType;
        this.mPushToken = pushToken;
    }
}
