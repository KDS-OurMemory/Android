package com.skts.ourmemory.model.schedule;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.model.DeletePostResult;
import com.skts.ourmemory.model.UpdatePostResult;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.List;

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
     * 일정 추가 요청
     */
    @Override
    public void setAddScheduleData(int userId, String name, List<Integer> members, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, List<Integer> shareRooms, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        AddSchedulePost addSchedulePost = new AddSchedulePost(userId, name, members, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor, shareRooms);
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
                        DebugLog.d(TAG, "Success");
                        mPresenter.getAddScheduleResult(addSchedulePostResultData);          // Success
                    }
                }));
    }

    /**
     * 일정 수정 요청
     */
    @Override
    public void putScheduleData(int memoryId, String name, List<Integer> members, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, List<Integer> shareRooms, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        EditSchedulePost editSchedulePost = new EditSchedulePost(name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
        Observable<UpdatePostResult> observable = service.putScheduleData(memoryId, editSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UpdatePostResult>() {
                    UpdatePostResult updatePostResultData;

                    @Override
                    public void onNext(@NonNull UpdatePostResult updatePostResult) {
                        DebugLog.i(TAG, updatePostResult.toString());
                        updatePostResultData = updatePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getPutScheduleResult(updatePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "Success");
                        mPresenter.getPutScheduleResult(updatePostResultData);          // Success
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
        Observable<DeletePostResult> observable = service.deleteScheduleData(memoryId, deleteSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DeletePostResult>() {
                    DeletePostResult deletePostResultData;

                    @Override
                    public void onNext(@NonNull DeletePostResult deletePostResult) {
                        DebugLog.i(TAG, deletePostResult.toString());
                        deletePostResultData = deletePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getDeleteScheduleResult(deletePostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "Success");
                        mPresenter.getDeleteScheduleResult(deletePostResultData);          // Success
                    }
                }));
    }
}
