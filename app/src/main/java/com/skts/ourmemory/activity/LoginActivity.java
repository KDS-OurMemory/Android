package com.skts.ourmemory.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.sessionCallback.SessionCallback;
import com.skts.ourmemory.util.DebugLog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    /*카카오*/
    private SessionCallback mSessionCallback = new SessionCallback();
    private Session mSession;
    private LinearLayout mButtonKakaoLogin;

    /*구글*/
    private static final int RC_SIGN_IN = 900;      // 구글로그인 result 상수
    private GoogleSignInClient mGoogleSignInClient;  // 구글 api 클라이언트
    private FirebaseAuth mFirebaseAuth;              // 파이어베이스 인증 객체 생성
    private LinearLayout mButtonGoogleLogin;              // 구글 로그인 버튼

    /*네이버*/
    private LinearLayout mButtonNaverLogin;
    private OAuthLogin mOAuthLogin;
    private RequestApiTask requestApiTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 카카오
        mSession = Session.getCurrentSession();
        mSession.addCallback(mSessionCallback);

        // 파이어베이스 인증 객체 선언
        mFirebaseAuth = FirebaseAuth.getInstance();

        // 구글 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // 네이버
        // 초기화
        mOAuthLogin = OAuthLogin.getInstance();
        mOAuthLogin.init(getApplicationContext(), getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.naver_client_name));

        // login button
        mButtonKakaoLogin = findViewById(R.id.btn_activity_login_kakao_custom_login);
        mButtonGoogleLogin = findViewById(R.id.btn_activity_login_google_custom_login);
        mButtonNaverLogin = findViewById(R.id.btn_activity_login_naver_custom_login);

        // set listener
        mButtonKakaoLogin.setOnClickListener(this);
        mButtonGoogleLogin.setOnClickListener(this);
        mButtonNaverLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_login_kakao_custom_login:
                mSession.open(AuthType.KAKAO_LOGIN_ALL, this);
                break;

            case R.id.btn_activity_login_google_custom_login:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            case R.id.btn_activity_login_naver_custom_login:
                mOAuthLogin.startOauthLoginActivity(this, mOAuthLoginHandler);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제 (카카오)
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        // 구글 로그인 버튼 응답
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //구글 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                DebugLog.e(TAG, "ApiException error : " + e);
            }
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase 에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, R.string.success_login, Toast.LENGTH_SHORT).show();
                        DebugLog.d(TAG,
                                "getDisplayName: " + mFirebaseAuth.getCurrentUser().getDisplayName() + ", " +
                                        "getEmail: " + mFirebaseAuth.getCurrentUser().getEmail()
                        );
                    } else {
                        // 로그인 실패
                        Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                    }

                });
    }

    @SuppressLint("HandlerLeak")
    private final OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Context context = getApplicationContext();
                String accessToken = mOAuthLogin.getAccessToken(context);
                String refreshToken = mOAuthLogin.getRefreshToken(context);
                long expiresAt = mOAuthLogin.getExpiresAt(context);
                String tokenType = mOAuthLogin.getTokenType(context);

                DebugLog.i(TAG, "accessToken : " + accessToken);
                DebugLog.i(TAG, "refreshToken : " + refreshToken);
                DebugLog.i(TAG, "expiresAt : " + expiresAt);
                DebugLog.i(TAG, "tokenType : " + tokenType);

                requestApiTask = new RequestApiTask();
                requestApiTask.execute();
            } else {
                Context context = getApplicationContext();
                String errorCode = mOAuthLogin.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLogin.getLastErrorDesc(context);

                DebugLog.e(TAG, "errorCode: " + errorCode + ", errorDesc: " + errorDesc);
                Toast.makeText(context, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            DebugLog.i(TAG, "onPreExecute");
        }

        @Override
        protected String doInBackground(Void... accessToken) {
            return mOAuthLogin.requestApi(getApplicationContext(), "test", ServerConst.NAVER_LOGIN_API_URL);
        }

        protected void onPostExecute(String content) {
            DebugLog.i(TAG, "onPreExecute, content: " + content);
        }
    }
}
