package com.skts.ourmemory.model.room;

public class RoomData {
    private final String roomTitle;
    private final String roomParticipants;

    public RoomData(String roomTitle, String roomParticipants) {
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
