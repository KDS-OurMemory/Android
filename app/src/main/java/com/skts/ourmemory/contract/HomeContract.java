package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeContract {
    public interface Model extends BaseContract.Model {
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);
        void getScheduleListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void showRoomList(ArrayList<String> names, List<List<UserDAO>> membersList);    // 방 데이터 표시
        void showCalendarList(ArrayList<String> todayList, ArrayList<String> nextList); // 일정 데이터 표시
        void showWeek();                                                                // 일주일 표시
        void showWeekHeader();                                                          // 일주일 날짜 표시
        void showWeekCalendar();                                                        // 일주일 일정 표시
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        // 폴링 데이터 가져오기
        void getPollingData();

        // 화면 상태에 따라 데이터 가져오기
        void getData(boolean hidden);
        
        // 방, 일정 데이터 가져오기
        void getRoomAndScheduleData();

        // 방 목록 가져오기
        void getRoomListResult(RoomPostResult roomPostResult);

        // 일정 목록 가져오기
        void getScheduleListResult(SchedulePostResult schedulePostResult);

        // 방 데이터
        void getRoomListData(RoomPostResult roomPostResult);

        // 일정 데이터
        void getCalendarListData(SchedulePostResult schedulePostResult);
    }
}
