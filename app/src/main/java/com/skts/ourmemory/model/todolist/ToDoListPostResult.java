package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

import java.util.List;

public class ToDoListPostResult extends BasePostResult {
    @SerializedName("response")
    private List<ToDoListDAO> response;

    @Override
    public String getResultCode() {
        return super.getResultCode();
    }

    @Override
    public String getResultMessage() {
        return super.getResultMessage();
    }

    @Override
    public String getDetailMessage() {
        return super.getDetailMessage();
    }

    @Override
    public String getResponseDate() {
        return super.getResponseDate();
    }

    public List<ToDoListDAO> getResponse() {
        return response;
    }
}
