package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.user.UserDAO;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.ArrayList;
import java.util.List;

public class HomeContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void showToast(String message);

        Context getAppContext();

        void initView(android.view.View view);                      // 초기 뷰 설정

        void initSet();                                             // 초기 설정

        void showRoomList(ArrayList<String> names, List<List<UserDAO>> membersList);    // 방 데이터 표시

        void showCalendarList(ArrayList<String> todayList, ArrayList<String> nextList); // 일정 데이터 표시

        void showWeekHeader();                                                          // 일주일 날짜 표시

        void showCalendar(SchedulePostResult schedulePostResult);                       // 일주일 일정 표시

        void showRoomData(RoomPostResult roomPostResult);                               // 방 데이터 표시
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        // 방 데이터
        void getRoomListData(RoomPostResult roomPostResult);

        // 일정 데이터
        void getCalendarListData(SchedulePostResult schedulePostResult);
    }
}
