package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.room.AddRoomPostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RoomContract {
    public interface Model extends BaseContract.Model {
        void getRoomData(int roomId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
        void showCalendar(AddRoomPostResult.ResponseValue responseValue);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정

        void getRoomData(int roomId);

        void getRoomDataResult(AddRoomPostResult addRoomPostResult);
    }
}
