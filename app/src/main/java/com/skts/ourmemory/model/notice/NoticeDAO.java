package com.skts.ourmemory.model.notice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NoticeDAO implements Serializable {
    @SerializedName("noticeId")
    private int noticeId;
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;
    @SerializedName("read")
    private boolean read;
    @SerializedName("regDate")
    private String regDate;

    public int getNoticeId() {
        return noticeId;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isRead() {
        return read;
    }

    public String getRegDate() {
        return regDate;
    }
}
