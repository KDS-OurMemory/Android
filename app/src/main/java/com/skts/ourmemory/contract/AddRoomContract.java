package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.adapter.AddRoomAdapter;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.EachRoomPostResult;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddRoomContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);                // 친구 데이터

        void setCreateRoomData(String roomName, int userId, ArrayList<Integer> friendIdList, boolean openedRoom, CompositeDisposable compositeDisposable);      // 방 생성 요청
    }

    public interface View extends BaseContract.View {
        Context getAppContext();

        void showToast(String message);

        void onBackPressed();

        void initSet();

        void setAddRoomAdapter(AddRoomAdapter addRoomAdapter);                  // 어댑터 설정

        void showNoFriend(boolean status);                                      // 친구 목록 없음 표시

        void checkCount(int count);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        AddRoomAdapter getAddRoomAdapter();

        void getFriendData();           // 친구 데이터 받기

        void getFriendListResult(FriendPostResult friendPostResult);            // 친구 목록 가져오기

        void setFriendData(FriendPostResult friendPostResult);                  // 친구 목록 데이터 설정

        void setCreateRoom(String roomName, ArrayList<Integer> friendIdList, boolean openedRoom);

        void setCreateRoomResult(EachRoomPostResult eachRoomPostResult);
    }
}
