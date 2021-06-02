package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.room.RoomPostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainContract {
    public interface Model extends BaseContract.Model {
        void getRoomListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int id);
        Context getAppContext();
        void showToast(String message);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        // 방 목록 가져오기
        void getRoomList();

        // 방 목록 가져오기
        void getRoomListResult(RoomPostResult roomPostResult);
    }
}
