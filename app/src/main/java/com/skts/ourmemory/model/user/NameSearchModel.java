package com.skts.ourmemory.model.user;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.NameContract;
import com.skts.ourmemory.model.friend.FriendDTO;
import com.skts.ourmemory.model.friend.FriendPostResult;
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
        Observable<FriendPostResult> observable = service.getUserDataName(userId, userName);

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
                                       mPresenter.getUserNameResult(friendPostResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "getUserData success");
                                       mPresenter.getUserNameResult(friendPostResultData);
                                   }
                               }
                ));
    }

    @Override
    public void addFriendData(int userId, int friendId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        FriendDTO friendDTO = new FriendDTO(userId, friendId);
        Observable<UserPostResult> observable = service.postRequestFriendData(friendDTO);

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
                                       mPresenter.getRequestFriendResult(userPostResultData);
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "addFriendData success");
                                       mPresenter.getRequestFriendResult(userPostResultData);
                                   }
                               }
                ));
    }
}
