package com.skts.ourmemory.model.friend;

public class Friend {
    int friendId;
    String profile;
    String name;
    String birthday;
    boolean solar;
    boolean birthdayOpen;
    boolean selectStatus;

    public Friend(int friendId, String profile, String name, String birthday, boolean solar, boolean birthdayOpen, boolean selectStatus) {
        this.friendId = friendId;
        this.profile = profile;
        this.name = name;
        this.birthday = birthday;
        this.solar = solar;
        this.birthdayOpen = birthdayOpen;
        this.selectStatus = selectStatus;
    }

    public int getFriendId() {
        return friendId;
    }

    public String getProfile() {
        return profile;
    }

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

    public boolean isSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }
}
