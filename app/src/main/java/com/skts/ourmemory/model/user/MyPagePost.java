package com.skts.ourmemory.model.user;

import com.google.gson.annotations.SerializedName;

public class MyPagePost {
    @SerializedName("name")
    private String name;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("solar")
    private boolean solar;
    @SerializedName("birthdayOpen")
    private boolean birthdayOpen;
    @SerializedName("push")
    private boolean push;

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean isSolar() {
        return solar;
    }

    public boolean isBirthdayOpen() {
        return birthdayOpen;
    }

    public boolean isPush() {
        return push;
    }

    public MyPagePost(String name, String birthday, boolean solar, boolean birthdayOpen, boolean push) {
        this.name = name;
        this.birthday = birthday;
        this.solar = solar;
        this.birthdayOpen = birthdayOpen;
        this.push = push;
    }
}
