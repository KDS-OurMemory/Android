package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.GlobalApplication;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.main.MainModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();

    public MainPresenter() {
        GlobalApplication globalApplication = new GlobalApplication();
        globalApplication.startThread();
        DebugLog.i(TAG, "전역 스레드 시작");
    }

    @Override
    public void setView(MainContract.View view) {
    }

    @Override
    public void releaseView() {
    }
}
