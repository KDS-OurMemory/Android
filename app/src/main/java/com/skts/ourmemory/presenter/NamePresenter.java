package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.NameContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.NameSearchModel;
import com.skts.ourmemory.model.user.UserPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class NamePresenter implements NameContract.Presenter {
    private final String TAG = NamePresenter.class.getSimpleName();

    private final NameContract.Model mModel;
    private NameContract.View mView;
    private MySharedPreferences mMySharedPreferences;
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
        if (name.trim().equals("")) {
            mView.showToast("올바른 이름을 입력해주세요.");
            return;
        }
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getUserData(userId, name, mCompositeDisposable);
    }

    @Override
    public void getUserNameResult(FriendPostResult friendPostResult) {
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
}
