package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

public class FriendPost {
    @SerializedName("fromUserId")
    private int fromUserId;                 // User id
    @SerializedName("toUserId")
    private int toUserId;                   // Friend id

    public FriendPost(int fromUserId, int toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }
}
