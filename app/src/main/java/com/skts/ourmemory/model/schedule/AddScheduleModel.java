package com.skts.ourmemory.model.schedule;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
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
    public void setAddScheduleData(int userId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        AddSchedulePost addSchedulePost = new AddSchedulePost(userId, name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
        Observable<AddSchedulePostResult> observable = service.postAddScheduleData(addSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddSchedulePostResult>() {
                    AddSchedulePostResult addSchedulePostResultData;

                    @Override
                    public void onNext(@NonNull AddSchedulePostResult addSchedulePostResult) {
                        DebugLog.i(TAG, addSchedulePostResult.toString());
                        addSchedulePostResultData = addSchedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getAddScheduleResult(addSchedulePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setAddScheduleData Success");
                        mPresenter.getAddScheduleResult(addSchedulePostResultData);          // Success
                    }
                }));
    }

    /**
     * 방 일정 추가 요청
     */
    @Override
    public void setAddRoomScheduleData(int userId, int roomId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        AddRoomSchedulePost addRoomSchedulePost = new AddRoomSchedulePost(userId, roomId, name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
        Observable<AddSchedulePostResult> observable = service.postAddRoomScheduleData(addRoomSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddSchedulePostResult>() {
                    AddSchedulePostResult addSchedulePostResultData;

                    @Override
                    public void onNext(@NonNull AddSchedulePostResult addSchedulePostResult) {
                        DebugLog.i(TAG, addSchedulePostResult.toString());
                        addSchedulePostResultData = addSchedulePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getAddScheduleResult(addSchedulePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setAddRoomScheduleData Success");
                        mPresenter.getAddScheduleResult(addSchedulePostResultData);          // Success
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
        Observable<BasicResponsePostResult> observable = service.putScheduleData(memoryId, userId, editSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BasicResponsePostResult>() {
                    BasicResponsePostResult postResultData;

                    @Override
                    public void onNext(@NonNull BasicResponsePostResult basicResponsePostResult) {
                        DebugLog.i(TAG, basicResponsePostResult.toString());
                        postResultData = basicResponsePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getPutScheduleResult(postResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "putScheduleData Success");
                        mPresenter.getPutScheduleResult(postResultData);          // Success
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
                        DebugLog.d(TAG, "Success");
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
        Observable<BasicResponsePostResult> observable = service.deleteScheduleData(memoryId, deleteSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BasicResponsePostResult>() {
                    BasicResponsePostResult postResultData;

                    @Override
                    public void onNext(@NonNull BasicResponsePostResult basicResponsePostResult) {
                        DebugLog.i(TAG, basicResponsePostResult.toString());
                        postResultData = basicResponsePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getDeleteScheduleResult(postResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "Success");
                        mPresenter.getDeleteScheduleResult(postResultData);          // Success
                    }
                }));
    }
}
