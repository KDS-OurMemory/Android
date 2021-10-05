package com.skts.ourmemory.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShareDAO implements Serializable {
    @SerializedName("type")
    private String type;
    @SerializedName("targetIds")
    private List<Integer> targetIds;

    public String getType() {
        return type;
    }

    public List<Integer> getTargetIds() {
        return targetIds;
    }

    public ShareDAO(String type, List<Integer> targetIds) {
        this.type = type;
        this.targetIds = targetIds;
    }
}
