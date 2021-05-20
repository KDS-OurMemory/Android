package com.skts.ourmemory.model.user;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.friend.AddFriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.List;

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
     * @param userId 사용자 번호
     */
    @Override
    public void getUserData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<UserPostResult> observable = service.getUserData(userId, "");

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserPostResult>() {
                                   String resultCode;
                                   String message;
                                   List<UserDAO> userDataList;

                                   @Override
                                   public void onNext(@NonNull UserPostResult userPostResult) {
                                       DebugLog.i(TAG, userPostResult.toString());
                                       resultCode = userPostResult.getResultCode();
                                       message = userPostResult.getMessage();
                                       userDataList = userPostResult.getResponse();
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getUserIdResultFail();       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getUserIdResultSuccess(resultCode, message, userDataList);
                                   }
                               }
                ));
    }

    @Override
    public void addFriendData(int userId, int friendId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<AddFriendPostResult> observable = service.postAddFriendData(userId, friendId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddFriendPostResult>() {
                                   String resultCode;
                                   String message;
                                   String addDate;

                                   @Override
                                   public void onNext(@NonNull AddFriendPostResult addFriendPostResult) {
                                       DebugLog.i(TAG, addFriendPostResult.toString());
                                       resultCode = addFriendPostResult.getResultCode();
                                       message = addFriendPostResult.getMessage();
                                       AddFriendPostResult.ResponseValue responseValue = addFriendPostResult.getResponseValue();
                                       if (responseValue != null) {
                                           addDate = responseValue.getAddDate();
                                       }
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getRequestFriendResultFail();       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getRequestFriendResultSuccess(resultCode, message, addDate);
                                   }
                               }
                ));
    }
}
