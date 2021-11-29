package com.skts.ourmemory.model.todolist;

import com.google.gson.annotations.SerializedName;

public class ToDoListDTO {
    @SerializedName("writerId")
    private Integer writerId;
    @SerializedName("contents")
    private String contents;
    @SerializedName("todoDate")
    private String todoDate;

    public ToDoListDTO() {
    }

    public ToDoListDTO(Integer writerId, String contents, String todoDate) {
        this.writerId = writerId;
        this.contents = contents;
        this.todoDate = todoDate;
    }
}
