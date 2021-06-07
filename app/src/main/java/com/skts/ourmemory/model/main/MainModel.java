package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainModel implements MainContract.Model {
    private final String TAG = MainModel.class.getSimpleName();

    private final MainContract.Presenter mPresenter;

    public MainModel(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 방 리스트 요청
     *
     * @param userId User id
     */
    @Override
    public void getRoomListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<RoomPostResult> observable = service.getRoomData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RoomPostResult>() {
                                   RoomPostResult roomPostResultData;

                                   @Override
                                   public void onNext(@NonNull RoomPostResult roomPostResult) {
                                       DebugLog.i(TAG, roomPostResult.toString());
                                       roomPostResultData = roomPostResult;
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getRoomListResult(roomPostResultData);       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getRoomListResult(roomPostResultData);
                                   }
                               }
                ));
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
