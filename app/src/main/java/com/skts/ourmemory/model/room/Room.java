package com.skts.ourmemory.model.room;

import com.skts.ourmemory.model.user.UserDAO;

import java.util.List;

public class Room {
    int roomId;
    int ownerId;
    String name;
    String regDate;
    boolean opened;
    List<UserDAO> members;
    boolean selectStatus;

    public Room(int roomId, int ownerId, String name, String regDate, boolean opened, List<UserDAO> members, boolean selectStatus) {
        this.roomId = roomId;
        this.ownerId = ownerId;
        this.name = name;
        this.regDate = regDate;
        this.opened = opened;
        this.members = members;
        this.selectStatus = selectStatus;
    }

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

    public List<UserDAO> getMembers() {
        return members;
    }

    public boolean isSelectStatus() {
        return selectStatus;
    }

    public void setSelectStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }
}
