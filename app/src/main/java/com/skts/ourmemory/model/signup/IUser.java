package com.skts.ourmemory.model.signup;

public interface IUser {
    String getId();
    String getName();
    String getBirthday();
    boolean getBirthdayType();
    boolean getBirthdayOpen();
    int getLoginType();
    String getProfileUrl();
    String getSignUpDate();
}