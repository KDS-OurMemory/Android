package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.IdSearchModel;
import com.skts.ourmemory.model.user.UserPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class IdPresenter implements IdContract.Presenter {
    private final String TAG = IdPresenter.class.getSimpleName();

    private final IdContract.Model mModel;
    private IdContract.View mView;
    private MySharedPreferences mMySharedPreferences;
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
    public void getUserId(int findId) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getUserData(userId, findId, mCompositeDisposable);
    }

    @Override
    public void getUserIdResult(FriendPostResult friendPostResult) {
        if (friendPostResult == null) {
            mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (friendPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 목록 조회 성공");
            int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
            mView.showUserList(userId, friendPostResult);
        } else {
            mView.showToast(friendPostResult.getResultMessage());
        }
    }

    @Override
    public void requestFriend(int friendId) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.addFriendData(userId, friendId, mCompositeDisposable);
    }

    @Override
    public void getRequestFriendResult(UserPostResult userPostResult) {
        if (userPostResult == null) {
            mView.showToast("친구 추가 요청 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (userPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 추가 요청 성공");
        } else {
            mView.showToast(userPostResult.getResultMessage());
        }
    }

    @Override
    public void cancelFriend(int friendId) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.cancelFriendData(userId, friendId, mCompositeDisposable);
    }

    @Override
    public void getCancelFriendResult(BasicResponsePostResult basicResponsePostResult) {
        if (basicResponsePostResult == null) {
            mView.showToast("친구 요청 취소 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (basicResponsePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 요청 취소 성공");
        } else {
            mView.showToast(basicResponsePostResult.getResultMessage());
        }
    }
}
