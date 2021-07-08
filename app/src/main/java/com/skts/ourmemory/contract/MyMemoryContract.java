package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.schedule.AddSchedulePostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MyMemoryContract {
    public interface Model extends BaseContract.Model {
        void getScheduleListData(int userId, CompositeDisposable compositeDisposable);
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
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        boolean isDuplicate();      // Remove duplicate clicks

        boolean isDuplicate2();     // Remove duplicate clicks

        void setYearMonth(int year, int month);     // Set year/month

        int getYear();              // Get year

        int getMonth();             // Get month

        void setAdapterModel(CalendarAdapterContract.Model adapterModel);

        void setAdapterView(CalendarAdapterContract.View adapterView);

        // 폴링 데이터 가져오기
        void getPollingData();

        // 화면 상태에 따라 데이터 가져오기
        void getData(boolean hidden);
        
        // 일정 데이터 가져오기
        void getScheduleData();

        // 일정 목록 가져오기
        void getScheduleListResult(SchedulePostResult schedulePostResult);

        // 일정 데이터
        void getCalendarListData(SchedulePostResult schedulePostResult);
    }
}
