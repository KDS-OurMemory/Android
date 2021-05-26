package com.skts.ourmemory.model;

public class SelectPerson {
    int friendId;
    String profile;
    String name;
    int index;

    public SelectPerson(int friendId, String profile, String name, int index) {
        this.friendId = friendId;
        this.profile = profile;
        this.name = name;
        this.index = index;
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

    public int getIndex() {
        return index;
    }
}
