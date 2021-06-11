package com.skts.ourmemory.model.room;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;
import com.skts.ourmemory.model.UserDAO;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomPostResult extends BasePostResult {
    @SerializedName("response")
    private List<ResponseValue> responseValueList;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public List<ResponseValue> getResponseValueList() {
        return responseValueList;
    }

    public static class ResponseValue {
        @SerializedName("roomId")
        private int roomId;
        @SerializedName("owner")
        private int owner;
        @SerializedName("name")
        private String name;
        @SerializedName("regDate")
        private String regDate;
        @SerializedName("opened")
        private boolean opened;
        @SerializedName("members")
        private List<UserDAO> memberList;

        public int getRoomId() {
            return roomId;
        }

        public int getOwner() {
            return owner;
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

        public List<UserDAO> getMemberList() {
            return memberList;
        }

        @Override
        public String toString() {
            return "ResponseValue{" +
                    "roomId=" + roomId +
                    ", owner='" + owner + '\'' +
                    ", name='" + name + '\'' +
                    ", regDate='" + regDate + '\'' +
                    ", opened=" + opened +
                    ", member=" + memberList +
                    '}';
        }
    }
}
