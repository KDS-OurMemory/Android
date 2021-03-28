package com.skts.ourmemory.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.AuthCredential;
import com.skts.ourmemory.BaseContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginContract {

    public interface Model extends BaseContract.Model {
        void setIntroData(String userId, CompositeDisposable compositeDisposable);
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
        void setView(View View);

        @Override
        void releaseView();

        @Override
        boolean isDuplicate();

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
        void checkSignUp(String id);

        void getServerResult(int result);     // 서버 응답 결과 처리 함수
    }
}
