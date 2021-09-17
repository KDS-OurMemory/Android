package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ShareContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
        void showFriendData(FriendPostResult friendPostResult);
        void showRoomData(RoomPostResult roomPostResult);
        FriendPostResult getFriendData();
        RoomPostResult getRoomData();
        int getPrivateRoomId();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정

        public FriendPostResult getFriendPostResult();

        public RoomPostResult getRoomPostResult();

        void getFriendListResult(FriendPostResult friendPostResult);            // 친구 목록 가져오기
        
        void getRoomListResult(RoomPostResult roomPostResult);                  // 방 목록 가져오기

        int getPrivateRoomId();
    }
}
