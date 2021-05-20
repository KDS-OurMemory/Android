package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.user.IdSearchModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class IdPresenter implements IdContract.Presenter {
    private final String TAG = IdPresenter.class.getSimpleName();

    private final IdContract.Model mModel;
    private IdContract.View mView;

    private MySharedPreferences mMySharedPreferences;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public IdPresenter() {
        this.mModel = new IdSearchModel(this);
    }

    @Override
    public void setView(IdContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getUserId(int userId) {
        mModel.getUserData(userId, mCompositeDisposable);
    }

    @Override
    public void getUserIdResultFail() {
        mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getUserIdResultSuccess(String resultCode, String message, List<UserDAO> userData) {
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
