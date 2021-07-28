package com.skts.ourmemory.model.friend;

import com.skts.ourmemory.contract.FriendContract;

public class FriendModel implements FriendContract.Model {
    private final FriendContract.Presenter mPresenter;

    public FriendModel(FriendContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
