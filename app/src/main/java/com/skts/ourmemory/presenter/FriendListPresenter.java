package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.FriendListContract;
import com.skts.ourmemory.model.friend.FriendListModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendListPresenter implements FriendListContract.Presenter {
    private final String TAG = FriendListPresenter.class.getSimpleName();

    private final FriendListContract.Model mModel;
    private FriendListContract.View mView;
    private MySharedPreferences mMySharedPreferences;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public FriendListPresenter() {
        this.mModel = new FriendListModel(this);
    }

    @Override
    public void setView(FriendListContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }

    @Override
    public void getFriendList() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getFriendListData(userId, mCompositeDisposable);
    }

    @Override
    public void getFriendListResultFail() {
        mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getFriendListResultSuccess(String resultCode, String message, ArrayList<Integer> userIds, ArrayList<String> names) {
        DebugLog.i(TAG, "친구 목록 조회 성공");
        mView.showFriendList(userIds, names);
    }
}
