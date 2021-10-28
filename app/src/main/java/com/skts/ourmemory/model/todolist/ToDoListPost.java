package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;

public class ToDoListPost {
    @SerializedName("writer")
    int writer;
    @SerializedName("contents")
    String contents;
    @SerializedName("todoDate")
    String todoDate;

    public ToDoListPost(int writer, String contents, String todoDate) {
        this.writer = writer;
        this.contents = contents;
        this.todoDate = todoDate;
    }
}
