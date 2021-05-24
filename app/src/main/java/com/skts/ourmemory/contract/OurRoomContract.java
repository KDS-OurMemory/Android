package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.room.RoomPostResult;

import java.util.List;

public class OurRoomContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void showRoomList(List<RoomPostResult.ResponseValue> responseValueList);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
