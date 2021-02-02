package com.skts.ourmemory.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.kakao.auth.AuthType;
import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.signup.SignUpActivity;

import butterknife.BindView;

public class LoginSampleActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {
    private final String TAG = LoginActivity.class.getSimpleName();

    private LoginPresenter mLoginPresenter = new LoginPresenter(this.getBaseContext(), this.getApplicationContext());

    /*카카오*/
    @BindView(R.id.btn_activity_login_kakao_custom_login)
    private LinearLayout mButtonKakaoLogin;             // 카카오 로그인 버튼

    /*구글*/
    @BindView(R.id.btn_activity_login_google_custom_login)
    private LinearLayout mButtonGoogleLogin;            // 구글 로그인 버튼

    /*네이버*/
    @BindView(R.id.btn_activity_login_naver_custom_login)
    private LinearLayout mButtonNaverLogin;             // 네이버 로그인 버튼


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // presenter 와 연결
        mLoginPresenter.setView(this);

        // 카카오 설정
        mLoginPresenter.setKakaoApi();

        // 구글 설정
        mLoginPresenter.setGoogleApi(this);
        
        // 네이버 설정


        // set listener
        mButtonKakaoLogin.setOnClickListener(this);
        mButtonGoogleLogin.setOnClickListener(this);
        mButtonNaverLogin.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (mLoginPresenter.mSession.handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        // 구글 로그인 버튼 응답
        if (requestCode == ServerConst.RC_SIGN_IN) {
            mLoginPresenter.loadGoogleApi(data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 카카오 세션 콜백 삭제
        mLoginPresenter.removeCallback();

        // presenter 와의 연결을 해제합니다.
        mLoginPresenter.releaseView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_login_kakao_custom_login:
                mLoginPresenter.mSession.open(AuthType.KAKAO_LOGIN_ALL, this);
                break;

            case R.id.btn_activity_login_google_custom_login:
                Intent signInIntent = mLoginPresenter.mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, ServerConst.RC_SIGN_IN);
                break;

            case R.id.btn_activity_login_naver_custom_login:
                mLoginPresenter.mOAuthLogin.startOauthLoginActivity(this, naverLoginHandler());
                break;
        }
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase 에 인증합니다.
    @Override
    public void firebaseAuthWithGoogle(AuthCredential authCredential) {
        mLoginPresenter.mFirebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        DebugLog.i(TAG, "구글 로그인 성공");
                        mLoginPresenter.passToFirebaseData();
                    } else {
                        // 로그인 실패
                        Toast.makeText(LoginSampleActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void startSignUpActivity(String id, String name, String birthday, int loginType) {
        Intent intent = new Intent(LoginSampleActivity.this, SignUpActivity.class);
        intent.putExtra(Const.USER_ID, id);
        intent.putExtra(Const.USER_NAME, name);
        intent.putExtra(Const.USER_BIRTHDAY, birthday);
        intent.putExtra(Const.USER_LOGIN_TYPE, loginType);
        startActivity(intent);
    }

    /*지울 것*/
    /*// 카카오 로그아웃 처리
    public void kakaoLogout(View v) {
        *//*UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });*//*
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(Long result) {
            }
        });
    }

    // 구글 로그아웃 처리
    public void googleLogout(View v) {
        mFirebaseAuth.signOut();
    }

    // 로그아웃 처리(토큰도 함께 삭제)
    public void naverLogout(View v) {
        NaverApiDeleteToken naverApiDeleteToken = new NaverApiDeleteToken(getApplicationContext(), mOAuthLogin);
        naverApiDeleteToken.execute();
    }

    public void giveServerData(View v) {
        String url = "http://dykim.ddns.net:8080/SignUp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userType", "3");
            jsonObject.put("userNickname", "오광석");
            jsonObject.put("userBirthday", "0602");
            jsonObject.put("userBirthdayOpen", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "누름", Toast.LENGTH_SHORT).show();

        *//*NetworkTask networkTask = new NetworkTask(url, jsonObject.toString());
        networkTask.execute();*//*
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private String values;

        public NetworkTask(String url, String values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }*/
}
