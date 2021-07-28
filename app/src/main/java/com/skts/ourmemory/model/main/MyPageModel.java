package com.skts.ourmemory.model.main;

import com.skts.ourmemory.contract.MyPageContract;

public class MyPageModel implements MyPageContract.Model {
    private final MyPageContract.Presenter mPresenter;

    public MyPageModel(MyPageContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
