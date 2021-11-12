package com.skts.ourmemory.presenter;

import com.skts.ourmemory.adapter.AddRoomAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.model.friend.Friend;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.AddRoomModel;
import com.skts.ourmemory.model.room.EachRoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddRoomPresenter implements AddRoomContract.Presenter {
    private final String TAG = AddRoomPresenter.class.getSimpleName();

    private final AddRoomContract.Model mModel;
    private AddRoomContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    AddRoomAdapter mAddRoomAdapter;

    public AddRoomPresenter() {
        this.mModel = new AddRoomModel(this);
    }

    @Override
    public AddRoomAdapter getAddRoomAdapter() {
        return mAddRoomAdapter;
    }

    @Override
    public void setView(AddRoomContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        // 친구 데이터 불러오기
        getFriendData();
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getFriendData() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getFriendListData(userId, mCompositeDisposable);
    }

    @Override
    public void getFriendListResult(FriendPostResult friendPostResult) {
        if (friendPostResult == null) {
            mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (friendPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 목록 조회 성공");
            setFriendData(friendPostResult);
        } else {
            mView.showToast(friendPostResult.getMessage());
        }
    }

    @Override
    public void setFriendData(FriendPostResult friendPostResult) {
        List<FriendPostResult.ResponseValue> friendData = friendPostResult.getResponse();
        if (friendData.isEmpty()) {
            // 친구 목록 없음
            mView.showNoFriend(true);
            return;
        }
        mView.showNoFriend(false);      // 친구 목록 없음 표시 숨기기
        ArrayList<Friend> friendList = new ArrayList<>();      // 친구 목록
        for (int i = 0; i < friendData.size(); i++) {
            FriendPostResult.ResponseValue responseValue = friendData.get(i);
            String status = responseValue.getStatus();
            if (status.equals(ServerConst.FRIEND)) {
                // 친구
                Friend friend = new Friend(
                        responseValue.getFriendId(),
                        responseValue.getProfileImageUrl(),
                        responseValue.getName(),
                        responseValue.getBirthday(),
                        responseValue.isSolar(),
                        responseValue.isBirthdayOpen(),
                        false
                );
                friendList.add(friend);
            }
        }

        mAddRoomAdapter = new AddRoomAdapter(friendList);
        mView.setAddRoomAdapter(mAddRoomAdapter);
    }

    @Override
    public void setCreateRoom(String roomName, ArrayList<Integer> friendIdList, boolean openedRoom) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.setCreateRoomData(roomName, userId, friendIdList, openedRoom, mCompositeDisposable);
    }

    @Override
    public void setCreateRoomResult(EachRoomPostResult eachRoomPostResult) {
        if (eachRoomPostResult == null) {
            mView.showToast("방 생성 요청 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (eachRoomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            mView.showToast("방 생성 요청 성공");
            mView.onBackPressed();
        } else {
            mView.showToast(eachRoomPostResult.getMessage());
        }
    }
}
