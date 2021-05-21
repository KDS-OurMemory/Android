package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.FriendListContract;

public class FriendListPresenter implements FriendListContract.Presenter {
    private final String TAG = FriendListPresenter.class.getSimpleName();

    private FriendListContract.View mView;

    @Override
    public void setView(FriendListContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
