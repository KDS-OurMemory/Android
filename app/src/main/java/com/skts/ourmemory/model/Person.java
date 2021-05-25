package com.skts.ourmemory.model;

public class Person {
    int friendId;
    String profile;
    String name;

    public Person(int friendId, String profile, String name) {
        this.friendId = friendId;
        this.profile = profile;
        this.name = name;
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
}
