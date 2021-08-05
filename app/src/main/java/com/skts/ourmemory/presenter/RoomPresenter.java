package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.RoomModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RoomPresenter implements RoomContract.Presenter {
    private final RoomContract.Model mModel;
    private RoomContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public RoomPresenter() {
        this.mModel = new RoomModel(this);
    }

    @Override
    public void setView(RoomContract.View view) {
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
