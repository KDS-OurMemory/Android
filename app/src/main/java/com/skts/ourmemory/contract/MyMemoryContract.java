package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.schedule.AddSchedulePostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.GregorianCalendar;

public class MyMemoryContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void initView(android.view.View view);                      // 초기 뷰 설정
        void initSet();                                             // 초기 설정
        void initCalendarList();                                    // 초기 캘린더 설정
        int getLastWeek(int year, int month);                       // 해당 월이 총 몇 주인지 구하는 함수
        void setRecycler();                                         // Recycler view 설정
        void setCalendarList(GregorianCalendar cal);                // 캘린더 리스트 설정
        void actionUpLayout();                                      // ACTION_UP event
        void actionMoveLayout(float getY, int firstTouch);          // ACTION_MOVE event
        void updateCalendarData(AddSchedulePostResult addSchedulePostResult);   // 캘린더 데이터 업데이트
        void showScheduleData(SchedulePostResult schedulePostResult);           // 일정 데이터 불러오기
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        boolean isDuplicate();      // Remove duplicate clicks

        boolean isDuplicate2();     // Remove duplicate clicks

        void setDate(int year, int month, int day);     // Set date

        int getYear();              // Get year

        int getMonth();             // Get month

        int getDay();               // Get day

        void setDay(int day);       // Set day

        void setAdapterModel(CalendarAdapterContract.Model adapterModel);

        void setAdapterView(CalendarAdapterContract.View adapterView);
    }
}
