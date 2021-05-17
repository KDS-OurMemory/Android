package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.OurMemoryContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OurMemoryPresenter implements OurMemoryContract.Presenter {
    private final String TAG = OurMemoryPresenter.class.getSimpleName();

    private OurMemoryContract.View mView;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public OurMemoryPresenter() {
    }

    @Override
    public void setView(OurMemoryContract.View view) {
        mView = view;
    }

    @Override
    public void releaseView() {
        mView = null;
        this.mCompositeDisposable.dispose();
    }
}
