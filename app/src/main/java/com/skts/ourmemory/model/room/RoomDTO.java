package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomDTO {
    @SerializedName("name")
    String name;
    @SerializedName("userId")
    int userId;
    @SerializedName("member")
    List<Integer> member;
    @SerializedName("opened")
    boolean opened;

    public RoomDTO() {
    }

    public RoomDTO(String name, int userId, List<Integer> member, boolean opened) {
        this.name = name;
        this.userId = userId;
        this.member = member;
        this.opened = opened;
    }
}
