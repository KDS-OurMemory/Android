package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyMemoryModel implements MyMemoryContract.Model {
    private final String TAG = MyMemoryModel.class.getSimpleName();
    private final MyMemoryContract.Presenter mPresenter;

    public MyMemoryModel(MyMemoryContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 일정 리스트 요청
     *
     * @param userId User id
     */
    @Override
    public void getScheduleListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<SchedulePostResult> observable = service.getScheduleDataId(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SchedulePostResult>() {
                    SchedulePostResult schedulePostResultData;

                    @Override
                    public void onNext(@NonNull SchedulePostResult schedulePostResult) {
                        DebugLog.i(TAG, schedulePostResult.toString());
                        schedulePostResultData = schedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, "getScheduleListData" + e.getMessage());
                        mPresenter.getScheduleListResult(schedulePostResultData);       // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getScheduleListData Success");
                        mPresenter.getScheduleListResult(schedulePostResultData);
                    }
                }));
    }
}
