package com.skts.ourmemory.model;

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