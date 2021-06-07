package com.skts.ourmemory.model.schedule;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SchedulePostResult extends BasePostResult {
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

    public List<ResponseValue> getResponse() {
        return response;
    }

    public static class ResponseValue {
        @SerializedName("memoryId")
        private int memoryId;
        @SerializedName("writerId")
        private int writerId;
        @SerializedName("name")
        private String name;
        @SerializedName("contents")
        private String contents;
        @SerializedName("place")
        private String place;
        @SerializedName("startDate")
        private String startDate;
        @SerializedName("endDate")
        private String endDate;
        @SerializedName("bgColor")
        private String bgColor;
        @SerializedName("firstAlarm")
        private String firstAlarm;
        @SerializedName("secondAlarm")
        private String secondAlarm;
        @SerializedName("regDate")
        private String regDate;
        @SerializedName("modDate")
        private String modDate;
        @SerializedName("members")
        private List<Member> members;

        public int getMemoryId() {
            return memoryId;
        }

        public int getWriterId() {
            return writerId;
        }

        public String getName() {
            return name;
        }

        public String getContents() {
            return contents;
        }

        public String getPlace() {
            return place;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getBgColor() {
            return bgColor;
        }

        public String getFirstAlarm() {
            return firstAlarm;
        }

        public String getSecondAlarm() {
            return secondAlarm;
        }

        public String getRegDate() {
            return regDate;
        }

        public String getModDate() {
            return modDate;
        }

        public List<Member> getMembers() {
            return members;
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
