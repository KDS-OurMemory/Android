package com.skts.ourmemory.model.login;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.LoginContract;
import com.skts.ourmemory.util.DebugLog;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginModel implements LoginContract.Model {
    private final String TAG = LoginModel.class.getSimpleName();

    private final LoginContract.Presenter mPresenter;

    /*생성자*/

    public LoginModel(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 회원가입 여부 확인
     *
     * @param userId              snsId
     * @param compositeDisposable RxJava 관련
     */
    @Override
    public void setIntroData(String userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<LoginPostResult> observable = service.getIntroData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginPostResult>() {
                                   String resultCode;
                                   String message;
                                   String id;
                                   String name;
                                   String birthday;
                                   boolean isSolar;
                                   boolean isBirthdayOpen;

                                   @Override
                                   public void onNext(@NonNull LoginPostResult loginPostResult) {
                                       DebugLog.i(TAG, loginPostResult.toString());
                                       resultCode = loginPostResult.getResultCode();
                                       message = loginPostResult.getMessage();
                                       LoginPostResult.ResponseValue responseValue = loginPostResult.getResponse();
                                       id = responseValue.getId();
                                       name = responseValue.getName();
                                       birthday = responseValue.getBirthday();
                                       isSolar = responseValue.isSolar();
                                       isBirthdayOpen = responseValue.isBirthdayOpen();
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getServerResultFail();        // 실패
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "성공");
                                       // resultCode 처리
                                       mPresenter.getServerResultSuccess(resultCode, message, id, name, birthday, isSolar, isBirthdayOpen);
                                   }
                               }
                ));
    }
}
