package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.AddRoomContract;

public class AddRoomPresenter implements AddRoomContract.Presenter {
    private AddRoomContract.View mView;

    @Override
    public void setView(AddRoomContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
