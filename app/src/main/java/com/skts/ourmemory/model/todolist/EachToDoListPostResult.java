package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class EachToDoListPostResult extends BasePostResult {
    @SerializedName("response")
    private ToDoListDAO response;

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

    public ToDoListDAO getResponse() {
        return response;
    }
}
