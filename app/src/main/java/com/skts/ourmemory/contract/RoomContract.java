package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.room.EachRoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;

import java.util.GregorianCalendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RoomContract {
    public interface Model extends BaseContract.Model {
        void getRoomData(int roomId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        Context getAppContext();

        void showToast(String message);

        void initSet();

        void setRecycler();

        void setNavigationView();

        void setCalendarList(GregorianCalendar cal);

        void showCalendar(RoomResponseValue roomResponseValue);

        void updateCalendarData(MemoryDAO memoryDAO, String mode);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void setDate(int year, int month, int day);     // 날짜 설정

        int getYear();              // Get year

        int getMonth();             // Get month

        int getDay();               // Get day

        void setDay(int day);       // Set day

        int getRoomId();            // Get Room id

        void setRoomId(int roomId); // Set Room id

        void initSet();                 // 초기 설정

        void getRoomData(int roomId);

        void getRoomDataResult(EachRoomPostResult eachRoomPostResult);

        int getLastWeek(int year, int month);

        String convertSolarToLunar();       // 양력 날짜 음력 변환
    }
}
