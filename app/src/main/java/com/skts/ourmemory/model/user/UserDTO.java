package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @SerializedName("snsId")
    String snsId;
    @SerializedName("name")
    String name;
    @SerializedName("birthday")
    String birthday;
    @SerializedName("solar")
    boolean solar;
    @SerializedName("birthdayOpen")
    boolean birthdayOpen;
    @SerializedName("snsType")
    Integer snsType;
    @SerializedName("push")
    boolean push;
    @SerializedName("pushToken")
    String pushToken;
    @SerializedName("deviceOs")
    String deviceOs;

    public UserDTO() {
    }

    public UserDTO(String pushToken) {
        this.pushToken = pushToken;
    }

    public UserDTO(String name, String birthday, boolean solar, boolean birthdayOpen, boolean push) {
        this.name = name;
        this.birthday = birthday;
        this.solar = solar;
        this.birthdayOpen = birthdayOpen;
        this.push = push;
    }

    public UserDTO(String snsId, String name, String birthday, boolean solar, boolean birthdayOpen, int snsType, boolean push, String pushToken, String deviceOs) {
        this.snsId = snsId;
        this.name = name;
        this.birthday = birthday;
        this.solar = solar;
        this.birthdayOpen = birthdayOpen;
        this.snsType = snsType;
        this.push = push;
        this.pushToken = pushToken;
        this.deviceOs = deviceOs;
    }
}
