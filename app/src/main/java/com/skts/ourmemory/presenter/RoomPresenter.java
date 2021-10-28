package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.RoomModel;
import com.skts.ourmemory.model.room.AddRoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RoomPresenter implements RoomContract.Presenter {
    private final String TAG = RoomPresenter.class.getSimpleName();

    private final RoomContract.Model mModel;
    private RoomContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public RoomPresenter() {
        this.mModel = new RoomModel(this);
    }

    @Override
    public void setView(RoomContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        initSet();
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void initSet() {
    }

    @Override
    public void getRoomData(int roomId) {
        mModel.getRoomData(roomId, mCompositeDisposable);
    }

    @Override
    public void getRoomDataResult(AddRoomPostResult addRoomPostResult) {
        if (addRoomPostResult == null) {
            mView.showToast("방 데이터 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (addRoomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            // Success
            DebugLog.i(TAG, "방 데이터 조회 성공");
            AddRoomPostResult.ResponseValue responseValue = addRoomPostResult.getResponseValueList();
            mView.showCalendar(responseValue);
        } else {
            mView.showToast(addRoomPostResult.getMessage());
        }
    }
}
