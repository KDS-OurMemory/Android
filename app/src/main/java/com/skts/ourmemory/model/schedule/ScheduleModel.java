package com.skts.ourmemory.model.schedule;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.ScheduleContract;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScheduleModel implements ScheduleContract.Model {
    private final String TAG = ScheduleModel.class.getSimpleName();

    private final ScheduleContract.Presenter mPresenter;

    public ScheduleModel(ScheduleContract.Presenter presenter) {
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
        Observable<SchedulePostResult> observable = service.getScheduleData(userId);

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
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getScheduleListResult(schedulePostResultData);       // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "Success");
                        mPresenter.getScheduleListResult(schedulePostResultData);
                    }
                }));
    }
}
