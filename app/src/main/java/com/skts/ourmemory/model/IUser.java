package com.skts.ourmemory.model;

public interface IUser {
    String getId();
    String getName();
    String getBirthday();
    int getBirthdayType();
    boolean getBirthdayOpen();
    int getLoginType();
    String getProfileUrl();
    String getJoinDate();
}