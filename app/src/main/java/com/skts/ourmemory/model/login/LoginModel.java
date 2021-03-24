package com.skts.ourmemory.model.login;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.LoginContract;
import com.skts.ourmemory.util.DebugLog;

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
                                   @Override
                                   public void onNext(@NonNull LoginPostResult loginPostResult) {
                                       DebugLog.i(TAG, loginPostResult.toString());
                                       mPresenter.getServerResult(ServerConst.ON_NEXT);         // 서버 결과
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getServerResult(ServerConst.ON_ERROR);        // 서버 결과
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "성공");
                                       // resultCode 처리
                                       mPresenter.getServerResult(ServerConst.ON_COMPLETE);     // 서버 결과
                                   }
                               }

                ));
    }
}
