package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.MainContract;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.View mView;

    public MainPresenter() {
    }

    @Override
    public void setView(MainContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
