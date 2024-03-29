package com.skts.ourmemory.model.room;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRoomModel implements AddRoomContract.Model {
    private final String TAG = AddRoomModel.class.getSimpleName();
    private final AddRoomContract.Presenter mPresenter;

    public AddRoomModel(AddRoomContract.Presenter presenter) {
        this.mPresenter = presenter;
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

    /**
     * 방 생성
     */
    @Override
    public void setCreateRoomData(String roomName, int userId, ArrayList<Integer> friendIdList, boolean openedRoom, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        RoomDTO roomDTO = new RoomDTO(roomName, userId, friendIdList, openedRoom);
        Observable<EachRoomPostResult> observable = service.postRoomData(roomDTO);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<EachRoomPostResult>() {
                    EachRoomPostResult eachRoomPostResultData;

                    @Override
                    public void onNext(@NonNull EachRoomPostResult eachRoomPostResult) {
                        DebugLog.i(TAG, eachRoomPostResult.toString());
                        eachRoomPostResultData = eachRoomPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.setCreateRoomResult(eachRoomPostResultData);     // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setCreateRoomData Success");
                        mPresenter.setCreateRoomResult(eachRoomPostResultData);     // Success
                    }
                }));
    }
}
