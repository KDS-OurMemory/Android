package com.skts.ourmemory.model.ourmemory;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OurMemoryModel implements OurMemoryContract.Model {
    private final String TAG = OurMemoryModel.class.getSimpleName();

    private final OurMemoryContract.Presenter mPresenter;

    /*Constructor*/
    public OurMemoryModel(OurMemoryContract.Presenter presenter) {
        this.mPresenter = presenter;
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
                                   String resultCode;
                                   String message;
                                   List<FriendPostResult.ResponseValue> responseValueList;

                                   @Override
                                   public void onNext(@NonNull FriendPostResult friendPostResult) {
                                       DebugLog.i(TAG, friendPostResult.toString());
                                       resultCode = friendPostResult.getResultCode();
                                       message = friendPostResult.getMessage();
                                       responseValueList = friendPostResult.getResponse();
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getFriendListResultFail();       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getFriendListResultSuccess(resultCode, message, responseValueList);
                                   }
                               }

                ));
    }

    /**
     * 방 리스트 요청
     *
     * @param userId User id
     */
    @Override
    public void getRoomListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<RoomPostResult> observable = service.getRoomDataId(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RoomPostResult>() {
                                   String resultCode;
                                   String message;
                                   List<RoomPostResult.ResponseValue> responseValueList;

                                   @Override
                                   public void onNext(@NonNull RoomPostResult roomPostResult) {
                                       DebugLog.i(TAG, roomPostResult.toString());
                                       resultCode = roomPostResult.getResultCode();
                                       message = roomPostResult.getMessage();
                                       responseValueList = roomPostResult.getResponseValueList();
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getRoomListResultFail();       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getRoomListResultSuccess(resultCode, message, responseValueList);
                                   }
                               }

                ));
    }
}
