package com.skts.ourmemory.model;

import com.skts.ourmemory.contract.BucketListContract;

public class BucketListModel implements BucketListContract.Model {
    private final BucketListContract.Presenter mPresenter;

    public BucketListModel(BucketListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
