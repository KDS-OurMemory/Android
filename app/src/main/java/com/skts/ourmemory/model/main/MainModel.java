package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
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
        mPresenter = presenter;
    }

    /**
     * 일정 리스트 요청
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

    /**
     * 방 리스트 요청
     */
    @Override
    public void getRoomListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<RoomPostResult> observable = service.getRoomDataId(userId);

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
                        DebugLog.e(TAG, "getRoomListData" + e.getMessage());
                        mPresenter.getRoomListResult(roomPostResultData);                // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getRoomListData Success");
                        mPresenter.getRoomListResult(roomPostResultData);
                    }
                }));
    }

    /**
     * 친구 리스트 요청
     */
    @Override
    public void getFriendListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<FriendPostResult> observable = service.getFriendData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FriendPostResult>() {
                    FriendPostResult friendPostResultData;

                    @Override
                    public void onNext(@NonNull FriendPostResult friendPostResult) {
                        DebugLog.i(TAG, friendPostResult.toString());
                        friendPostResultData = friendPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getFriendListResult(friendPostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getFriendListData Success");
                        mPresenter.getFriendListResult(friendPostResultData);           // Success
                    }
                }));
    }
}
