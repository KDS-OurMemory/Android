package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.RecommendContract;

public class RecommendPresenter implements RecommendContract.Presenter {
    private RecommendContract.View mView;

    @Override
    public void setView(RecommendContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
