package com.skts.ourmemory.view.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.skts.ourmemory.BaseContract;

public class LoginContract {
    // View 와 Presenter 에 관한 내용을 한 눈에 볼 수 있음.

    interface View extends BaseContract.View {
        void showToast(String message);
        void startSignUpActivity(String id, String name, String birthday, int loginType);
        void firebaseAuthWithGoogle(AuthCredential authCredential);

    }

    interface Presenter extends BaseContract.Presenter<View> {

        @Override
        void setView(View View);

        @Override
        void releaseView();

        // 카카오 api 설정
        void setKakaoApi();

        // 카카오 로그인
        void loadKakaoApi();

        // 구글 api 설정
        void setGoogleApi(Activity activity);

        // 구글 로그인
        void loadGoogleApi(Intent intent);

        // 파이어베이스 데이터 전달
        void passToFirebaseData();

        // 네이버 api 설정
        void setNaverApi();
        
        // 네이버 로그인
        void loadNaverApi(Activity activity);

        // 카카오 세션 콜백 삭제
        void removeCallback();

        // 네이버 로그인 핸들러
        OAuthLoginHandler naverLoginHandler();
    }
}
