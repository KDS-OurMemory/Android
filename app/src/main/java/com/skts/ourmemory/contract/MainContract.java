package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainContract {
    public interface Model extends BaseContract.Model {
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);
        void getScheduleListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void setInitFragment();
        void setInitPollingData();
        void switchFragment(int id);
        Context getAppContext();
        void showToast(String message);
        void showCalendarList(SchedulePostResult schedulePostResult);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        // 폴링 데이터 가져오기
        void getPollingData();

        // 방 목록 가져오기
        void getRoomListResult(RoomPostResult roomPostResult);

        // 일정 목록 가져오기
        void getScheduleListResult(SchedulePostResult schedulePostResult);
    }
}
