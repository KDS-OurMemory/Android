package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

public class FriendStatusPost {
    @SerializedName("userId")
    private final int userId;                   // User id
    @SerializedName("friendUserId")
    private final int friendUserId;             // Friend id
    @SerializedName("status")
    private final String status;                // Status

    public FriendStatusPost(int userId, int friendUserId, String status) {
        this.userId = userId;
        this.friendUserId = friendUserId;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public int getFriendUserId() {
        return friendUserId;
    }

    public String getStatus() {
        return status;
    }
}
