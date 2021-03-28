package com.skts.ourmemory.model.addschedule;

import com.google.gson.annotations.SerializedName;

public class AddSchedulePostResult {
    @SerializedName("success")
    private boolean success;            // 성공 여부
    @SerializedName("errorCode")
    private String errorCode;           // 결과 코드 값
    @SerializedName("addDate")
    private String addDate;             // 일정 추가 날짜

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    @Override
    public String toString() {
        return "RoomDetailPostResult{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", addDate='" + addDate + '\'' +
                '}';
    }
}
