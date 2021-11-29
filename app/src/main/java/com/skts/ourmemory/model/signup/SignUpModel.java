package com.skts.ourmemory.model.signup;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.SignUpContract;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.model.user.UserDTO;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpModel implements SignUpContract.Model {
    private final String TAG = SignUpModel.class.getSimpleName();

    private final SignUpContract.Presenter mSignUpPresenter;

    /*Constructor*/
    public SignUpModel(SignUpContract.Presenter signUpPresenter) {
        this.mSignUpPresenter = signUpPresenter;
    }

    /**
     * 회원가입 요청
     *
     * @param snsId               sns id
     * @param userName            사용자명
     * @param userBirthday        사용자 생일
     * @param userBirthdayType    사용자 생일 타입(양/음력)
     * @param userBirthdayOpen    사용자 생일 공개 여부
     * @param userLoginType       sns 로그인 유형 (카카오/구글/네이버)
     * @param compositeDisposable RxJava 관련
     */
    @Override
    public void setSignUpData(String snsId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int userLoginType, String firebaseToken, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        UserDTO userDTO = new UserDTO(snsId, userName, userBirthday, userBirthdayType, userBirthdayOpen, userLoginType, true, firebaseToken, ServerConst.ANDROID);
        Observable<MyPagePostResult> observable = service.postSignUpData(userDTO);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyPagePostResult>() {
                    MyPagePostResult myPagePostResultData;

                    @Override
                    public void onNext(@NonNull MyPagePostResult myPagePostResult) {
                        DebugLog.i(TAG, myPagePostResult.toString());
                        myPagePostResultData = myPagePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mSignUpPresenter.getSignUpResult(myPagePostResultData);          // fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setSignUpData Success");
                        mSignUpPresenter.getSignUpResult(myPagePostResultData);          // Success
                    }
                }));
    }
}
