package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.main.MainModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.Model mModel;
    private MainContract.View mView;

    private MySharedPreferences mMySharedPreferences;

    /*RxJava*/
    private CompositeDisposable mCompositeDisposable;

    public MainPresenter() {
        this.mModel = new MainModel(this);
    }

    @Override
    public void setView(MainContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getRoomList() {
        mCompositeDisposable = new CompositeDisposable();
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        // TODO
        // 핸들러 스레드
        

        mModel.getRoomListData(userId, mCompositeDisposable);
    }

    @Override
    public void getRoomListResult(RoomPostResult roomPostResult) {
        if (roomPostResult == null) {
            mView.showToast("방 생성 요청 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (roomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "방 목록 조회 성공");

            // 내장 DB 저장
            // TODO
        } else {
            mView.showToast(roomPostResult.getMessage());
        }
    }
}
