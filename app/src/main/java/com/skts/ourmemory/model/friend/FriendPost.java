package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

public class FriendPost {
    @SerializedName("userId")
    private int userId;                 // User id
    @SerializedName("friendId")
    private int friendId;               // Friend id

    public FriendPost(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
