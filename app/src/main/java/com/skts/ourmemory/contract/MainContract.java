package com.skts.ourmemory.contract;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainContract {
    public interface Model extends BaseContract.Model {
        void getScheduleListData(int userId, CompositeDisposable compositeDisposable);      // 일정 데이터
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);          // 방 데이터
        void getUserData(int userId, CompositeDisposable compositeDisposable);              // 사용자 정보 데이터
    }

    public interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int id);
        Context getAppContext();
        void showToast(String message);
        void checkAlarm();
        void startAddScheduleActivity();
        void startAddRoomActivity();
        void startAddFriendActivity();
        void startEditMyPageActivity();
        FragmentManager getMyFragmentManager();
        RoomPostResult getRoomData();
        SchedulePostResult getScheduleData();
        MyPagePostResult getMyPageData();
        void showRoomData();
        void showScheduleData();
        void showMyPageData();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        int checkAlarmCount();          // 알람 카운트 확인

        int checkFriendRequestCount();  // 친구 요청 카운트 확인

        void startPolling();            // 폴링 데이터 받기 시작

        void getPollingData();          // 폴링 데이터(일정, 방)
        
        void getMyPageData();           // 사용자 정보 데이터

        void getRoomListResult(RoomPostResult roomPostResult);                  // 방 목록 가져오기

        void getScheduleListResult(SchedulePostResult schedulePostResult);      // 일정 목록 가져오기
        
        void getMyPageResult(MyPagePostResult myPagePostResult);                // 사용자 정보 가져오기

        boolean exitApp();                                                      // App exit
    }
}
