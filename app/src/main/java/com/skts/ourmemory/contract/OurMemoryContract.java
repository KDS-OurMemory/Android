package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;

import java.util.List;

public class OurMemoryContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void showToast(String message);

        Context getAppContext();

        void initView(android.view.View view);                      // 초기 뷰 설정

        void initSet();                                             // 초기 설정

        void showNoRoom(boolean status);                            // 방 목록 없음 표시

        void showRoomData(List<RoomResponseValue> responseValues);  // 방 데이터 표시
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
