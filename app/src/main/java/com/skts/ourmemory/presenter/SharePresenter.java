package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.ShareContract;
import com.skts.ourmemory.model.ShareModel;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SharePresenter implements ShareContract.Presenter {
    private final String TAG = SharePresenter.class.getSimpleName();

    private ShareContract.Model mModel;
    private ShareContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private FriendPostResult mFriendPostResult;
    private RoomPostResult mRoomPostResult;

    @Override
    public void setView(ShareContract.View view) {
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
        mModel = new ShareModel(this);
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.getFriendListData(userId, mCompositeDisposable);     // 친구 목록 가져오기
        mModel.getRoomListData(userId, mCompositeDisposable);       // 방 목록 가져오기
    }

    @Override
    public FriendPostResult getFriendPostResult() {
        return mFriendPostResult;
    }

    @Override
    public RoomPostResult getRoomPostResult() {
        return mRoomPostResult;
    }

    @Override
    public void getFriendListResult(FriendPostResult friendPostResult) {
        if (friendPostResult == null) {
            mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (friendPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 목록 조회 성공");
            mFriendPostResult = friendPostResult;
            mView.showFriendData(friendPostResult);
        } else {
            mView.showToast(friendPostResult.getResultMessage());
        }
    }

    @Override
    public void getRoomListResult(RoomPostResult roomPostResult) {
        if (roomPostResult == null) {
            mView.showToast("방 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (roomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "방 목록 조회 성공");
            mRoomPostResult = roomPostResult;
            mView.showRoomData(roomPostResult);
        } else {
            mView.showToast(roomPostResult.getResultMessage());
        }
    }

    @Override
    public int getPrivateRoomId() {
        return mMySharedPreferences.getIntExtra(Const.PRIVATE_ROOM_ID);
    }
}
