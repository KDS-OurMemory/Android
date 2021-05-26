package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateRoomPost {
    @SerializedName("name")
    String mRoomName;
    @SerializedName("owner")
    int mOwner;
    @SerializedName("member")
    ArrayList<Integer> mMember;
    @SerializedName("opened")
    boolean mOpened;

    public CreateRoomPost(String roomName, int owner, ArrayList<Integer> member, boolean opened) {
        this.mRoomName = roomName;
        this.mOwner = owner;
        this.mMember = member;
        this.mOpened = opened;
    }
}
