package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.QRContract;

public class QRPresenter implements QRContract.Presenter {
    private QRContract.View mView;

    @Override
    public void setView(QRContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
