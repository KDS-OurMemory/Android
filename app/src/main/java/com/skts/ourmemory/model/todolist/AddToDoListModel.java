package com.skts.ourmemory.model.todolist;

import com.skts.ourmemory.contract.AddToDoListContract;

public class AddToDoListModel implements AddToDoListContract.Model {
    private final AddToDoListContract.Presenter mPresenter;

    public AddToDoListModel(AddToDoListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
