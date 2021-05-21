package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.NameContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.user.NameSearchModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class NamePresenter implements NameContract.Presenter {
    private final String TAG = NamePresenter.class.getSimpleName();

    private final NameContract.Model mModel;
    private NameContract.View mView;

    private MySharedPreferences mMySharedPreferences;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public NamePresenter() {
        this.mModel = new NameSearchModel(this);
    }

    @Override
    public void setView(NameContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getUserName(String name) {
        mModel.getUserData(name, mCompositeDisposable);
    }

    @Override
    public void getUserNameResultFail() {
        mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getUserNameResultSuccess(String resultCode, String message, List<UserDAO> userData) {
        DebugLog.i(TAG, "친구 목록 조회 성공");
        mView.showUserList(userData);
    }

    @Override
    public void requestFriend(int friendId) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.addFriendData(userId, friendId, mCompositeDisposable);
    }

    @Override
    public void getRequestFriendResultFail() {
        mView.showToast("친구 추가 요청 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getRequestFriendResultSuccess(String resultCode, String message, String addDate) {
        DebugLog.i(TAG, "친구 추가 요청 성공");
        mView.showToast("친구 추가 요청 성공");
    }
}
