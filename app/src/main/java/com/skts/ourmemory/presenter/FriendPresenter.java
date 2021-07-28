package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.model.friend.FriendModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendPresenter implements FriendContract.Presenter {
    private final FriendContract.Model mModel;
    private FriendContract.View mView;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private MySharedPreferences mMySharedPreferences;

    public FriendPresenter() {
        this.mModel = new FriendModel(this);
    }

    @Override
    public void setView(FriendContract.View view) {
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
        // 친구 뱃지 카운트 없애기
        mMySharedPreferences.putIntExtra(Const.FRIEND_REQUEST_COUNT, 0);
    }
}
