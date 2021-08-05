package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

public class FriendPost {
    @SerializedName("userId")
    private final int userId;                   // User id
    @SerializedName("friendUserId")
    private final int friendUserId;             // Friend id

    public FriendPost(int userId, int friendUserId) {
        this.userId = userId;
        this.friendUserId = friendUserId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFriendUserId() {
        return friendUserId;
    }
}
