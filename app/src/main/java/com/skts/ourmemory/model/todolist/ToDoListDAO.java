package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ToDoListDAO {
    @SerializedName("todoId")
    private int todoId;
    @SerializedName("writerId")
    private int writerId;
    @SerializedName("contents")
    private String contents;
    @SerializedName("todoDate")
    private String todoDate;
    @SerializedName("used")
    private boolean used;

    public int getTodoId() {
        return todoId;
    }

    public int getWriterId() {
        return writerId;
    }

    public String getContents() {
        return contents;
    }

    public String getTodoDate() {
        return todoDate;
    }

    public boolean isUsed() {
        return used;
    }
}
