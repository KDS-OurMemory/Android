package com.skts.ourmemory.model.main;

import com.skts.ourmemory.contract.MyMemoryContract;

public class MyMemoryModel implements MyMemoryContract.Model {
    private final String TAG = MyMemoryModel.class.getSimpleName();
    private final MyMemoryContract.Presenter mPresenter;

    public MyMemoryModel(MyMemoryContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
