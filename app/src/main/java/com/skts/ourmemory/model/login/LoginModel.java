package com.skts.ourmemory.model.login;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
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
     * @param snsId               sns id
     * @param compositeDisposable RxJava 관련
     */
    @Override
    public void setIntroData(String snsId, int loginType, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<LoginPostResult> observable = service.getIntroData(snsId, loginType);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginPostResult>() {
                    LoginPostResult loginPostResultData;

                    @Override
                    public void onNext(@NonNull LoginPostResult loginPostResult) {
                        DebugLog.i(TAG, loginPostResult.toString());
                        loginPostResultData = loginPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getLoginResult(loginPostResultData, loginType);        // fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "Login success");
                        mPresenter.getLoginResult(loginPostResultData, loginType);       // Success
                    }
                }));
    }

    /**
     * Request server token refresh
     *
     * @param userId              user id
     * @param compositeDisposable Relation RxJava
     */
    @Override
    public void setPatchData(int userId, String savedToken, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<PatchPostResult> observable = service.patchIntroData(userId, savedToken);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PatchPostResult>() {
                    PatchPostResult patchPostResultData;

                    @Override
                    public void onNext(@NonNull PatchPostResult patchPostResult) {
                        DebugLog.i(TAG, patchPostResult.toString());
                        patchPostResultData = patchPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getPatchResult(patchPostResultData);     // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "Patch success");
                        mPresenter.getPatchResult(patchPostResultData);     // Success
                    }
                }));
    }
}
