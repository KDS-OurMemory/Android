package com.skts.ourmemory.model;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.room.EachRoomPostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RoomModel implements RoomContract.Model {
    private final String TAG = RoomModel.class.getSimpleName();
    private final RoomContract.Presenter mPresenter;

    public RoomModel(RoomContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 개별 방 정보 요청
     */
    @Override
    public void getRoomData(int roomId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<EachRoomPostResult> observable = service.getEachRoomData(roomId);

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
                        mPresenter.getRoomDataResult(eachRoomPostResultData);          // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getRoomData Success");
                        mPresenter.getRoomDataResult(eachRoomPostResultData);          // Success
                    }
                }));
    }
}
