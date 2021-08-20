package com.skts.ourmemory.model.todolist;

import com.skts.ourmemory.contract.ToDoListContract;

public class ToDoListModel implements ToDoListContract.Model {
    private final ToDoListContract.Presenter mPresenter;

    public ToDoListModel(ToDoListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
