package com.skts.ourmemory.contract;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainContract {
    public interface Model extends BaseContract.Model {
        void getScheduleListData(int userId, CompositeDisposable compositeDisposable);      // 일정 데이터
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);          // 방 데이터
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);        // 친구 데이터
    }

    public interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int id);
        Context getAppContext();
        void showToast(String message);
        void startAddScheduleActivity();
        void startAddRoomActivity();
        FragmentManager getMyFragmentManager();
        RoomPostResult getRoomData();
        SchedulePostResult getScheduleData();
        FriendPostResult getFriendData();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void startPollingData();        // 폴링 데이터 받기 시작

        void getPollingData();          //폴링 데이터(일정, 방, 친구)

        void getRoomListResult(RoomPostResult roomPostResult);                  // 방 목록 가져오기

        void getScheduleListResult(SchedulePostResult schedulePostResult);      // 일정 목록 가져오기

        void getFriendListResult(FriendPostResult friendPostResult);            // 친구 목록 가져오기
    }
}
