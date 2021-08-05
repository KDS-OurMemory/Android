package com.skts.ourmemory.model;

import com.skts.ourmemory.contract.RoomContract;

public class RoomModel implements RoomContract.Model {
    private final RoomContract.Presenter mPresenter;

    public RoomModel(RoomContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
