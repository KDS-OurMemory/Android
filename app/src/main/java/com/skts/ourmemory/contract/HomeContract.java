package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomeContract {
    public interface Model extends BaseContract.Model {
        void getRoomList(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void setCalendarList(SchedulePostResult schedulePostResult);
        void showRoomList(Context context);
        void showCalendarList(ArrayList<String> todayList, ArrayList<String> nextList);
        void addRoomList(ArrayList<String> names, List<List<UserDAO>> membersList);
        void showWeek();                                                                // 일주일 표시
        void showWeekHeader();                                                          // 일주일 날짜 표시
        void showWeekCalendar();                                                        // 일주일 일정 표시
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void getRoomList(Context context);

        void getRoomListResultFail();

        void getRoomListResultSuccess(String resultCode, String message,
                                      ArrayList<Integer> roomIds,
                                      ArrayList<Integer> owners,
                                      ArrayList<String> names,
                                      ArrayList<String> regDates,
                                      ArrayList<Boolean> openedList,
                                      List<List<UserDAO>> membersList
        );

        void setCalendarList(SchedulePostResult schedulePostResult);
    }
}
