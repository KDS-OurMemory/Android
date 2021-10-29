package com.skts.ourmemory.model.todolist;

public class ToDoListData extends ToDoListViewModel {
    private int toDoListId;             // 일정 번호(서버 index)
    private String content;             // 내용
    private String date;                // 날짜
    private boolean finishState;        // 완료 상태

    public ToDoListData() {
    }

    public ToDoListData(int toDoListId, String content, String date, boolean finishState) {
        this.toDoListId = toDoListId;
        this.content = content;
        this.date = date;
        this.finishState = finishState;
    }

    public int getToDoListId() {
        return toDoListId;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public boolean isFinishState() {
        return finishState;
    }

    public void setFinishState(boolean finishState) {
        this.finishState = finishState;
    }
}
