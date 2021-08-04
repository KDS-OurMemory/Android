package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.AlarmContract;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AlarmPresenter implements AlarmContract.Presenter {
    private AlarmContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    @Override
    public void setView(AlarmContract.View view) {
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
        // 알람 뱃지 카운트 없애기
        mMySharedPreferences.putIntExtra(Const.ALARM_COUNT, 0);
    }
}
