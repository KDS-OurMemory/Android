package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;

public class ToDoListPost {
    @SerializedName("todoId")
    private Integer todoId;
    @SerializedName("writerId")
    private Integer writerId;
    @SerializedName("contents")
    private String contents;
    @SerializedName("todoDate")
    private String todoDate;

    public ToDoListPost() {
    }

    /**
     * 추가
     */
    public ToDoListPost(Integer todoId, Integer writerId, String contents, String todoDate) {
        this.writerId = writerId;
        this.contents = contents;
        this.todoDate = todoDate;
    }

    /**
     * 수정
     */
    public ToDoListPost(Integer todoId, String contents, String todoDate) {
        this.todoId = todoId;
        this.contents = contents;
        this.todoDate = todoDate;
    }
}
