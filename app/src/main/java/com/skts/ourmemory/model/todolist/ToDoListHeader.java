package com.skts.ourmemory.model.todolist;

public class ToDoListHeader extends ToDoListViewModel {
    String header;
    long currentTime;

    public ToDoListHeader() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String time) {
        this.header = time;
    }
}
