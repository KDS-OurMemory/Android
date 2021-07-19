package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.model.ourmemory.OurMemoryModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OurMemoryPresenter implements OurMemoryContract.Presenter {
    private OurMemoryContract.View mView;

    public OurMemoryPresenter() {
    }

    @Override
    public void setView(OurMemoryContract.View view) {
        mView = view;
    }

    @Override
    public void releaseView() {
        mView = null;
    }
}
