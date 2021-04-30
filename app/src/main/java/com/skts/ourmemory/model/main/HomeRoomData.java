package com.skts.ourmemory.model.main;

public class HomeRoomData {
    private String roomTitle;
    private String roomParticipants;

    public HomeRoomData(String roomTitle, String roomParticipants) {
        this.roomTitle = roomTitle;
        this.roomParticipants = roomParticipants;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public String getRoomParticipants() {
        return roomParticipants;
    }
}
