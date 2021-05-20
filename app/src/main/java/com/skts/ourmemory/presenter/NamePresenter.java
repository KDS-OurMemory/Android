package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.NameContract;

public class NamePresenter implements NameContract.Presenter {
    private NameContract.View mView;

    @Override
    public void setView(NameContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
