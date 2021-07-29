package com.skts.ourmemory.model.user;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.friend.CancelFriendPostResult;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.RequestFriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IdSearchModel implements IdContract.Model {
    private final String TAG = IdSearchModel.class.getSimpleName();

    private final IdContract.Presenter mPresenter;

    public IdSearchModel(IdContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 사용자 정보 조회
     *
     * @param userId 사용자 id
     * @param findId 조회할 id
     */
    @Override
    public void getUserData(int userId, int findId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<UserPostResult> observable = service.getUserDataId(userId, findId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserPostResult>() {
                                   UserPostResult userPostResultData;

                                   @Override
                                   public void onNext(@NonNull UserPostResult userPostResult) {
                                       DebugLog.i(TAG, userPostResult.toString());
                                       userPostResultData = userPostResult;
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getUserIdResult(userPostResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getUserIdResult(userPostResultData);
                                   }
                               }
                ));
    }

    @Override
    public void addFriendData(int userId, int friendId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        FriendPost friendPost = new FriendPost(userId, friendId);
        Observable<RequestFriendPostResult> observable = service.postRequestFriendData(friendPost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RequestFriendPostResult>() {
                                   RequestFriendPostResult requestFriendPostResultData;

                                   @Override
                                   public void onNext(@NonNull RequestFriendPostResult requestFriendPostResult) {
                                       DebugLog.i(TAG, requestFriendPostResult.toString());
                                       requestFriendPostResultData = requestFriendPostResult;
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getRequestFriendResult(requestFriendPostResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getRequestFriendResult(requestFriendPostResultData);
                                   }
                               }
                ));
    }

    @Override
    public void cancelFriendData(int userId, int friendId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        FriendPost friendPost = new FriendPost(userId, friendId);
        Observable<CancelFriendPostResult> observable = service.deleteCancelFriendData(friendPost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CancelFriendPostResult>() {
                                   CancelFriendPostResult cancelFriendPostResultData;

                                   @Override
                                   public void onNext(@NonNull CancelFriendPostResult cancelFriendPostResult) {
                                       DebugLog.i(TAG, cancelFriendPostResult.toString());
                                       cancelFriendPostResultData = cancelFriendPostResult;
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getCancelFriendResult(cancelFriendPostResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getCancelFriendResult(cancelFriendPostResultData);
                                   }
                               }
                ));
    }
}
