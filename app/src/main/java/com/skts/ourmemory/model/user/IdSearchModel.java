package com.skts.ourmemory.model.user;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.friend.FriendPost;
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
        Observable<BasicResponsePostResult> observable = service.postRequestFriendData(friendPost);

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
                                       mPresenter.getRequestFriendResult(postResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getRequestFriendResult(postResultData);
                                   }
                               }
                ));
    }

    @Override
    public void cancelFriendData(int userId, int friendId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        FriendPost friendPost = new FriendPost(userId, friendId);
        Observable<BasicResponsePostResult> observable = service.deleteCancelFriendData(friendPost);

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
                                       mPresenter.getCancelFriendResult(postResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getCancelFriendResult(postResultData);
                                   }
                               }
                ));
    }
}
