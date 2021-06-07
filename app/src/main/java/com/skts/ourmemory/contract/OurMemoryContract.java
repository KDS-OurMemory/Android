package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OurMemoryContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();

        // 친구 목록 보여주기
        void showFriendList(List<FriendPostResult.ResponseValue> responseValueList);

        // 방 목록 보여주기
        void showRoomList(List<RoomPostResult.ResponseValue> responseValueList);

        // 친구 추가 액티비티
        void startAddFriendActivity();

        // 방 추가 액티비티
        void startAddRoomActivity();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        // 폴링 데이터(방 목록, 친구 목록)
        void getPollingData();

        // 친구 목록 가져오기 실패
        void getFriendListResultFail();

        // 친구 목록 가져오기 성공
        void getFriendListResultSuccess(String resultCode, String message, List<FriendPostResult.ResponseValue> responseValueList);

        // 방 목록 가져오기 실패
        void getRoomListResultFail();

        // 방 목록 가져오기 성공
        void getRoomListResultSuccess(String resultCode, String message, List<RoomPostResult.ResponseValue> responseValueList);
    }
}
