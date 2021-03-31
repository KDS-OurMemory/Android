package com.skts.ourmemory.model.signup;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.SignUpContract;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpModel implements SignUpContract.Model {
    private final String TAG = SignUpModel.class.getSimpleName();

    private final SignUpContract.Presenter mSignUpPresenter;

    /*생성자*/
    public SignUpModel(SignUpContract.Presenter signUpPresenter) {
        this.mSignUpPresenter = signUpPresenter;
    }

    /**
     * 회원가입 요청
     *
     * @param userId              snsId
     * @param userName            사용자명
     * @param userBirthday        사용자 생일
     * @param userBirthdayType    사용자 생일 타입(양/음력)
     * @param userBirthdayOpen    사용자 생일 공개 여부
     * @param userLoginType       sns 로그인 유형 (카카오/구글/네이버)
     * @param compositeDisposable RxJava 관련
     */
    @Override
    public void setSignUpData(String userId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int userLoginType, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        SignUpPost signUpPost = new SignUpPost(userId, userName, userBirthday, userBirthdayType, userBirthdayOpen, userLoginType, ServerConst.FIREBASE_PUSH_TOKEN);
        Observable<SignUpPostResult> observable = service.postSignUpData(signUpPost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SignUpPostResult>() {
                                   @Override
                                   public void onNext(@NonNull SignUpPostResult signUpPostResult) {
                                       DebugLog.i(TAG, signUpPostResult.toString());
                                       mSignUpPresenter.getServerResult(ServerConst.ON_NEXT);      // 서버 결과
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mSignUpPresenter.getServerResult(ServerConst.ON_ERROR);      // 서버 결과
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "성공");
                                       // resultCode 처리
                                       mSignUpPresenter.getServerResult(ServerConst.ON_COMPLETE);      // 서버 결과
                                   }
                               }

                ));
    }
}