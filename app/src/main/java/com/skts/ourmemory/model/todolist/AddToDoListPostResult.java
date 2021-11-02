package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;
import com.skts.ourmemory.model.BasePostResult;

public class AddToDoListPostResult extends BasePostResult {
    @SerializedName("response")
    private ToDoListDAO response;

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

    public ToDoListDAO getResponse() {
        return response;
    }
}
