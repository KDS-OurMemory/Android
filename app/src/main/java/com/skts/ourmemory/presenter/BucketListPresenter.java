package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.BucketListContract;
import com.skts.ourmemory.model.BucketListModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BucketListPresenter implements BucketListContract.Presenter {
    private final String TAG = BucketListPresenter.class.getSimpleName();

    private final BucketListContract.Model mModel;
    private BucketListContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public BucketListPresenter() {
        this.mModel = new BucketListModel(this);
    }

    @Override
    public void setView(BucketListContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        initSet();
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void initSet() {
    }
}
