package com.skts.ourmemory.model.friend;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FriendModel implements FriendContract.Model {
    private final String TAG = FriendModel.class.getSimpleName();
    private final FriendContract.Presenter mPresenter;

    public FriendModel(FriendContract.Presenter presenter) {
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
     * 친구 요청 수락
     */
    @Override
    public void postAcceptFriend(FriendPost friendPost, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<AcceptFriendPostResult> observable = service.postAcceptFriendData(friendPost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AcceptFriendPostResult>() {
                    AcceptFriendPostResult acceptFriendPostResultData;

                    @Override
                    public void onNext(@NonNull AcceptFriendPostResult acceptFriendPostResult) {
                        DebugLog.i(TAG, acceptFriendPostResult.toString());
                        acceptFriendPostResultData = acceptFriendPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getAcceptFriendResult(acceptFriendPostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "postAcceptFriend Success");
                        mPresenter.getAcceptFriendResult(acceptFriendPostResultData);           // Success
                    }
                }));
    }
}
