package com.skts.ourmemory.model.friend;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendPostResult extends BasePostResult {
    @SerializedName("response")
    private List<ResponseValue> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
    }

    public List<ResponseValue> getResponse() {
        return response;
    }

    public static class ResponseValue {
        @SerializedName("friendId")
        private int friendId;
        @SerializedName("name")
        private String name;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("solar")
        private boolean solar;
        @SerializedName("birthdayOpen")
        private boolean birthdayOpen;
        @SerializedName("status")
        private String status;
        @SerializedName("profileImageUrl")
        private String profileImageUrl;

        public int getFriendId() {
            return friendId;
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

        public String getStatus() {
            return status;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }
    }
}
