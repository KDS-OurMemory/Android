package com.skts.ourmemory.contract;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainContract {
    public interface Model extends BaseContract.Model {
        void getScheduleListData(int userId, CompositeDisposable compositeDisposable);      // 일정 데이터

        void getRoomListData(int userId, CompositeDisposable compositeDisposable);          // 방 데이터

        void getUserData(int userId, CompositeDisposable compositeDisposable);              // 사용자 정보 데이터

        void deleteRoomData(int roomId, int userId, CompositeDisposable compositeDisposable);   // 방 데이터 삭제
    }

    public interface View extends BaseContract.View {
        void setInitFragment();

        void switchFragment(int id);

        Context getAppContext();

        void showToast(String message);

        void checkAlarm();                              // 알람 체크

        void startAddScheduleActivity(MemoryDAO memoryDAO, int year, int month, int day, String purpose);       // 일정 추가/수정

        void startAddRoomActivity();                    // 방 추가

        void startAddFriendActivity();                  // 친구 추가

        void startEditMyPageActivity();                 // 마이페이지 수정

        void startDeleteMyPageActivity();               // 회원 탈퇴

        void startRoomActivity(int position);           // 선택한 방 보여주기

        void deleteRoomData(int position);              // 선택한 방 삭제

        FragmentManager getMyFragmentManager();

        List<RoomResponseValue> getRoomData();

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

        void deleteRoomData(int roomId);                                        // 방 삭제

        void deleteRoomDataResult(BasicResponsePostResult basicResponsePostResult);     // 방 삭제 결과
    }
}
