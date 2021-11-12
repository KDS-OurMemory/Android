package com.skts.ourmemory.model.schedule;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddScheduleModel implements AddScheduleContract.Model {
    private final String TAG = AddScheduleModel.class.getSimpleName();

    private final AddScheduleContract.Presenter mPresenter;

    /*Constructor*/
    public AddScheduleModel(AddScheduleContract.Presenter addSchedulePresenter) {
        this.mPresenter = addSchedulePresenter;
    }

    /**
     * 개인 일정 추가 요청
     */
    @Override
    public void setAddScheduleData(int userId, Integer roomId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        SchedulePost schedulePost = new SchedulePost(userId, roomId, name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
        Observable<EachSchedulePostResult> observable = service.postAddScheduleData(schedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EachSchedulePostResult>() {
                    EachSchedulePostResult eachSchedulePostResultData;

                    @Override
                    public void onNext(@NonNull EachSchedulePostResult eachSchedulePostResult) {
                        DebugLog.i(TAG, eachSchedulePostResult.toString());
                        eachSchedulePostResultData = eachSchedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getAddScheduleResult(eachSchedulePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setAddScheduleData Success");
                        mPresenter.getAddScheduleResult(eachSchedulePostResultData);          // Success
                    }
                }));
    }

    /**
     * 방 일정 추가 요청
     */
    @Override
    public void setAddRoomScheduleData(int userId, Integer roomId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        SchedulePost schedulePost = new SchedulePost(userId, roomId, name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
        Observable<EachSchedulePostResult> observable = service.postAddRoomScheduleData(schedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EachSchedulePostResult>() {
                    EachSchedulePostResult eachSchedulePostResultData;

                    @Override
                    public void onNext(@NonNull EachSchedulePostResult eachSchedulePostResult) {
                        DebugLog.i(TAG, eachSchedulePostResult.toString());
                        eachSchedulePostResultData = eachSchedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getAddScheduleResult(eachSchedulePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setAddRoomScheduleData Success");
                        mPresenter.getAddScheduleResult(eachSchedulePostResultData);          // Success
                    }
                }));
    }

    /**
     * 일정 수정 요청
     */
    @Override
    public void putScheduleData(int memoryId, int userId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        EditSchedulePost editSchedulePost = new EditSchedulePost(name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
        Observable<EachSchedulePostResult> observable = service.putScheduleData(memoryId, userId, editSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EachSchedulePostResult>() {
                    EachSchedulePostResult eachSchedulePostResultData;

                    @Override
                    public void onNext(@NonNull EachSchedulePostResult eachSchedulePostResult) {
                        DebugLog.i(TAG, eachSchedulePostResult.toString());
                        eachSchedulePostResultData = eachSchedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getPutScheduleResult(eachSchedulePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "putScheduleData Success");
                        mPresenter.getPutScheduleResult(eachSchedulePostResultData);          // Success
                    }
                }));
    }

    /**
     * 친구 데이터 요청
     *
     * @param userId User id
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

    /**
     * 일정 삭제 요청
     */
    @Override
    public void deleteScheduleData(int memoryId, int userId, int targetRoomId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        DeleteSchedulePost deleteSchedulePost = new DeleteSchedulePost(userId, targetRoomId);
        Observable<EachSchedulePostResult> observable = service.deleteScheduleData(memoryId, deleteSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EachSchedulePostResult>() {
                    EachSchedulePostResult eachSchedulePostResultData;

                    @Override
                    public void onNext(@NonNull EachSchedulePostResult eachSchedulePostResult) {
                        DebugLog.i(TAG, eachSchedulePostResult.toString());
                        eachSchedulePostResultData = eachSchedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getDeleteScheduleResult(eachSchedulePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "deleteScheduleData Success");
                        mPresenter.getDeleteScheduleResult(eachSchedulePostResultData);          // Success
                    }
                }));
    }
}
