package com.skts.ourmemory.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.AuthCredential;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginContract {

    public interface Model extends BaseContract.Model {
        void setIntroData(String snsId, int snsType, CompositeDisposable compositeDisposable);
        void setPatchData(int userId, String savedToken, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        void firebaseAuthWithGoogle(AuthCredential authCredential);
        void startSignUpActivity(String id, String name, String birthday, int loginType);
        void startMainActivity();
        Context getAppContext();
    }

    public interface Presenter extends BaseContract.Presenter<View> {

        @Override
        void setView(View view);

        @Override
        void releaseView();

        // 카카오 api 설정
        void setKakaoApi();

        // 카카오 로그인
        void loadKakaoApi();

        // 카카오 세션 콜백 삭제
        void removeCallback();

        // 구글 api 설정
        void setGoogleApi(Activity activity);

        // 구글 로그인
        void loadGoogleApi(Intent intent);

        // 파이어베이스 데이터 전달
        void passToFirebaseData();

        // 네이버 api 설정
        void setNaverApi();
        
        // 네이버 로그인 파싱 데이터
        void processAuthResult(StringBuffer response);

        // 회원가입 여부 확인
        void checkSignUp(String id, String name, String birthday, int snsType);

        // 로그인 응답 실패
        void getLoginResultFail();

        // 로그인 응답 성공
        void getLoginResultSuccess(String resultCode, String message, int userId, String name, String birthday, boolean isSolar, boolean isBirthdayOpen, String pushToken);

        // 패치 응답 실패
        void getPatchResultFail();

        // 패치 응답 성공
        void getPatchResultSuccess(String resultCode, String message, String patchDate);
    }
}
