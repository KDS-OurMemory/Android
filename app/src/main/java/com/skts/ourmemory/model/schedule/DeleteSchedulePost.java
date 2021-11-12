package com.skts.ourmemory.model.schedule;

import com.google.gson.annotations.SerializedName;

public class DeleteSchedulePost {
    @SerializedName("userId")
    private int userId;                 // User id
    @SerializedName("targetRoomId")
    private int targetRoomId;

    public DeleteSchedulePost() {
    }

    public DeleteSchedulePost(int userId, int targetRoomId) {
        this.userId = userId;
        this.targetRoomId = targetRoomId;
    }

    public int getUserId() {
        return userId;
    }

    public int getTargetRoomId() {
        return targetRoomId;
    }
}
