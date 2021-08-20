package com.skts.ourmemory.model.todolist;

public class ToDoListData extends ToDoListViewModel {
    private boolean finishState;        // 완료 상태
    private String content;             // 내용
    private String date;                // 날짜

    public ToDoListData() {
    }

    public ToDoListData(boolean finishState, String content, String date) {
        this.finishState = finishState;
        this.content = content;
        this.date = date;
    }

    public boolean isFinishState() {
        return finishState;
    }

    public void setFinishState(boolean finishState) {
        this.finishState = finishState;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
