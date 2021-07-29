package com.skts.ourmemory.model.user;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.NameContract;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.RequestFriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NameSearchModel implements NameContract.Model {
    private final String TAG = NameSearchModel.class.getSimpleName();

    private final NameContract.Presenter mPresenter;

    public NameSearchModel(NameContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 사용자 정보 조회
     *
     * @param userId   사용자 id
     * @param userName 조회할 이름
     */
    @Override
    public void getUserData(int userId, String userName, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<UserPostResult> observable = service.getUserDataName(userId, userName);

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
                                       mPresenter.getUserNameResult(userPostResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getUserNameResult(userPostResultData);
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
}
