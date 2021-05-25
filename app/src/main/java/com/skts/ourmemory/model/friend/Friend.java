package com.skts.ourmemory.model.friend;

public class Friend {
    int friendId;
    String profile;
    String name;
    boolean selectStatus;

    public Friend(int friendId, String profile, String name, boolean selectStatus) {
        this.friendId = friendId;
        this.profile = profile;
        this.name = name;
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

    public boolean isSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }
}
