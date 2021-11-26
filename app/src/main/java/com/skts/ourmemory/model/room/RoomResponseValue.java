package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.friend.FriendDAO;
import com.skts.ourmemory.model.memory.MemoryDAO;

import java.io.Serializable;
import java.util.List;

public class RoomResponseValue implements Serializable {
    @SerializedName("roomId")
    private int roomId;
    @SerializedName("ownerId")
    private int ownerId;
    @SerializedName("name")
    private String name;
    @SerializedName("regDate")
    private String regDate;
    @SerializedName("opened")
    private boolean opened;
    @SerializedName("used")
    private boolean used;
    @SerializedName("members")
    private List<FriendDAO> memberList;
    @SerializedName("memories")
    private List<MemoryDAO> memoryList;

    public int getRoomId() {
        return roomId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public String getRegDate() {
        return regDate;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isUsed() {
        return used;
    }

    public List<FriendDAO> getMemberList() {
        return memberList;
    }

    public List<MemoryDAO> getMemoryList() {
        return memoryList;
    }
}
