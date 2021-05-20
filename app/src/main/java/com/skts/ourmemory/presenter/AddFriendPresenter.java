package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.AddFriendContract;

public class AddFriendPresenter implements AddFriendContract.Presenter {
    private AddFriendContract.View mView;

    @Override
    public void setView(AddFriendContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
