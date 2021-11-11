package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShareRoom implements Serializable {
    @SerializedName("roomId")
    private int roomId;
    @SerializedName("ownerId")
    private int ownerId;
    @SerializedName("name")
    private String name;

    public int getRoomId() {
        return roomId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }
}