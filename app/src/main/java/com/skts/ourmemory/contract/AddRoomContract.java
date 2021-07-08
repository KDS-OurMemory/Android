package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.room.RoomPostResult;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddRoomContract {
    public interface Model extends BaseContract.Model {
        void setCreateRoomData(String roomName, int userId, ArrayList<Integer> friendIdList, boolean openedRoom, CompositeDisposable compositeDisposable);      // 방 생성 요청
    }

    public interface View extends BaseContract.View {
        void setInitSetting();
        void checkCount(int count);
        Context getAppContext();
        void showToast(String message);
        void onBackPressed();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void setCreateRoom(String roomName, ArrayList<Integer> friendIdList, boolean openedRoom);

        void setCreateRoomResult(RoomPostResult roomPostResult);
    }
}
