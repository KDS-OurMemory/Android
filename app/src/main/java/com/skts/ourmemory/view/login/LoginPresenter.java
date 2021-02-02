package com.skts.ourmemory.view.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.Session;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.skts.ourmemory.R;
import com.skts.ourmemory.server.KakaoSessionCallback;
import com.skts.ourmemory.server.NaverApiMemberProfile;
import com.skts.ourmemory.util.DebugLog;

import java.util.concurrent.ExecutionException;

public class LoginPresenter implements LoginContract.Presenter {
    private final String TAG = LoginPresenter.class.getSimpleName();

    private LoginContract.View view;
    private Context mContext;
    private Context mApplicationContext;

    /*카카오*/
    private KakaoSessionCallback mKakaoSessionCallback;
    public Session mSession;

    /*구글*/
    public FirebaseAuth mFirebaseAuth;
    public GoogleSignInClient mGoogleSignInClient;     // 구글 api 클라이언트

    /*네이버*/
    private NaverApiMemberProfile mNaverApiMemberProfile;
    public OAuthLogin mOAuthLogin;

    LoginPresenter(Context context, Context applicationContext) {
        this.mContext = context;
        this.mApplicationContext = applicationContext;
    }

    @Override
    public void setView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    @Override
    public void setKakaoApi() {
        // 카카오
        mSession = Session.getCurrentSession();
        mKakaoSessionCallback = new KakaoSessionCallback();
        mSession.addCallback(mKakaoSessionCallback);
        //mSession.checkAndImplicitOpen();        //자동 로그인
    }

    @Override
    public void loadKakaoApi() {
    }

    @Override
    public void setGoogleApi(Activity activity) {
        // 구글 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions);
    }

    @Override
    public void loadGoogleApi(Intent intent) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
        try {
            //구글 로그인 성공
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            // 파이어베이스 인증 객체 선언
            mFirebaseAuth = FirebaseAuth.getInstance();
            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
            view.firebaseAuthWithGoogle(authCredential);
        } catch (ApiException e) {
            DebugLog.e(TAG, "ApiException error : " + e);
        }
    }

    @Override
    public void passToFirebaseData() {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String id = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        String birthday = null;
        int loginType = 2;
        view.startSignUpActivity(id, name, birthday, loginType);
    }

    @Override
    public void setNaverApi() {

    }

    @Override
    public void loadNaverApi(Activity activity) {
    }

    /*@Override
    public OAuthLoginHandler naverLoginHandler() {
        @SuppressLint("HandlerLeak")
        OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    String accessToken = mOAuthLogin.getAccessToken(mApplicationContext);
                    String refreshToken = mOAuthLogin.getRefreshToken(mApplicationContext);
                    long expiresAt = mOAuthLogin.getExpiresAt(mApplicationContext);
                    String tokenType = mOAuthLogin.getTokenType(mApplicationContext);

                    DebugLog.i(TAG, "accessToken : " + accessToken);
                    DebugLog.i(TAG, "refreshToken : " + refreshToken);
                    DebugLog.i(TAG, "expiresAt : " + expiresAt);
                    DebugLog.i(TAG, "tokenType : " + tokenType);

                    mNaverApiMemberProfile = new NaverApiMemberProfile(mApplicationContext, mOAuthLogin, accessToken);
                    try {
                        StringBuffer result = mNaverApiMemberProfile.execute().get();

                        // 로그인 처리가 완료되면 수행할 로직 작성
                        processAuthResult(result);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    String errorCode = mOAuthLogin.getLastErrorCode(mApplicationContext).getCode();
                    String errorDesc = mOAuthLogin.getLastErrorDesc(mApplicationContext);

                    DebugLog.e(TAG, "errorCode: " + errorCode + ", errorDesc: " + errorDesc);
                    Toast.makeText(mApplicationContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }
        };

        return oAuthLoginHandler;
    }*/

    @Override
    public void removeCallback() {
        // 세션 콜백 삭제 (카카오)
        mSession.removeCallback(mKakaoSessionCallback);
    }
}
