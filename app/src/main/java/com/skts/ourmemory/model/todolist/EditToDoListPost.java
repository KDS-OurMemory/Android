package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;

public class EditToDoListPost {
    @SerializedName("contents")
    String contents;
    @SerializedName("todoDate")
    String todoDate;

    public EditToDoListPost(String contents, String todoDate) {
        this.contents = contents;
        this.todoDate = todoDate;
    }
}
