package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditRoomPost {
    @SerializedName("name")
    String roomName;
    @SerializedName("opened")
    boolean opened;

    public EditRoomPost(String roomName, boolean opened) {
        this.roomName = roomName;
        this.opened = opened;
    }
}
