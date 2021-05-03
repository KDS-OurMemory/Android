package com.skts.ourmemory.model.main;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeRoomPostResult extends BasePostResult {
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

    @NotNull
    @Override
    public String toString() {
        return "LoginPostResult{" +
                "resultCode=" + super.getResultCode() +
                ", message='" + super.getMessage() + '\'' +
                /*", roomId=" + response.getRoomId() + '\'' +
                ", owner='" + response.getOwner() + '\'' +
                ", name='" + response.getName() + '\'' +
                ", regDate=" + response.getRegDate() + '\'' +
                ", opened=" + response.isOpened() + '\'' +*/
                '}';
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
        private List<Member> memberList;

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

        public List<Member> getMemberList() {
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

    public static class Member {
        @SerializedName("userId")
        private int userId;
        @SerializedName("name")
        private String name;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("solar")
        private boolean solar;
        @SerializedName("birthdayOpen")
        private boolean birthdayOpen;

        public int getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getBirthday() {
            return birthday;
        }

        public boolean isSolar() {
            return solar;
        }

        public boolean isBirthdayOpen() {
            return birthdayOpen;
        }
    }
}
