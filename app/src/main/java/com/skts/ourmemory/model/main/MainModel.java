package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;
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

    @Override
    public void getUserData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<MyPagePostResult> observable = service.getMyPageData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyPagePostResult>() {
                    MyPagePostResult myPagePostResultData;

                    @Override
                    public void onNext(@NonNull MyPagePostResult myPagePostResult) {
                        DebugLog.i(TAG, myPagePostResult.toString());
                        myPagePostResultData = myPagePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, "getUserData" + e.getMessage());
                        mPresenter.getMyPageResult(myPagePostResultData);                // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getUserData Success");
                        mPresenter.getMyPageResult(myPagePostResultData);
                    }
                }));
    }

    /**
     * 방 삭제
     */
    @Override
    public void deleteRoomData(int roomId, int userId, int position, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<BasicResponsePostResult> observable = service.deleteRoomData(roomId, userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BasicResponsePostResult>() {
                    BasicResponsePostResult basicResponsePostResultData;

                    @Override
                    public void onNext(@NonNull BasicResponsePostResult basicResponsePostResult) {
                        DebugLog.i(TAG, basicResponsePostResult.toString());
                        basicResponsePostResultData = basicResponsePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, "deleteRoomData" + e.getMessage());
                        mPresenter.deleteRoomDataResult(basicResponsePostResultData, position);             // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "deleteRoomData Success");
                        mPresenter.deleteRoomDataResult(basicResponsePostResultData, position);             // Success
                    }
                }));
    }
}
