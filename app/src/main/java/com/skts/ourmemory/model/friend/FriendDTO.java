package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;

public class FriendDTO {
    @SerializedName("friendUserId")
    private int friendUserId;
    @SerializedName("userId")
    private int userId;
    @SerializedName("friendStatus")
    private String friendStatus;

    public FriendDTO() {
    }

    public FriendDTO(int friendUserId, int userId) {
        this.friendUserId = friendUserId;
        this.userId = userId;
    }

    public FriendDTO(int friendUserId, int userId, String friendStatus) {
        this.friendUserId = friendUserId;
        this.userId = userId;
        this.friendStatus = friendStatus;
    }
}
