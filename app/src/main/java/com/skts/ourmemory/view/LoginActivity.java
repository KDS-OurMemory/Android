package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.auth.AuthType;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.LoginContract;
import com.skts.ourmemory.presenter.LoginPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;
import com.skts.ourmemory.view.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    private final String TAG = LoginActivity.class.getSimpleName();
    private final LoginContract.Presenter mLoginPresenter = new LoginPresenter();

    /*카카오*/
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_activity_login_kakao_custom_login)
    public LinearLayout mButtonKakaoLogin;             // 카카오 로그인 버튼

    /*구글*/
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_activity_login_google_custom_login)
    public LinearLayout mButtonGoogleLogin;            // 구글 로그인 버튼

    /*네이버*/
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_activity_login_naver_custom_login)
    public LinearLayout mButtonNaverLogin;             // 네이버 로그인 버튼

    MySharedPreferences mMySharedPreferences;

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
        mLoginPresenter.setNaverApi();

        mMySharedPreferences = MySharedPreferences.getInstance(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        DebugLog.w(TAG, "Fetching FCM registration token failed" + task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    mMySharedPreferences.putStringExtra(ServerConst.FIREBASE_PUSH_TOKEN, token);

                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    DebugLog.i(TAG, msg);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK 로 전달
        if (mLoginPresenter.getSession().handleActivityResult(requestCode, resultCode, data)) {
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

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_login_kakao_custom_login)
    void onClickKakao() {
        mLoginPresenter.getSession().open(AuthType.KAKAO_LOGIN_ALL, this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_login_google_custom_login)
    void onClickGoogle() {
        Intent signInIntent = mLoginPresenter.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, ServerConst.RC_SIGN_IN);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_login_naver_custom_login)
    void onClickNaver() {
        mLoginPresenter.getOAuthLogin().startOauthLoginActivity(this, mLoginPresenter.getOAuthLoginHandler());
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase 에 인증합니다.
    @Override
    public void firebaseAuthWithGoogle(AuthCredential authCredential) {
        mLoginPresenter.getFirebaseAuth().signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        DebugLog.i(TAG, "구글 로그인 성공");
                        mLoginPresenter.passToFirebaseData();
                    } else {
                        // 로그인 실패
                        Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 회원가입 액티비티로 이동
     *
     * @param snsId     sns id
     * @param name      이름
     * @param birthday  생일
     * @param loginType 로그인 유형
     */
    @Override
    public void startSignUpActivity(String snsId, String name, String birthday, int loginType) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra(Const.SNS_ID, snsId);
        intent.putExtra(Const.USER_NAME, name);
        intent.putExtra(Const.USER_BIRTHDAY, birthday);
        intent.putExtra(Const.USER_SNS_TYPE, loginType);
        startActivity(intent);
    }

    /**
     * 메인 액티비티로 이동
     */
    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getAppContext() {
        return super.getApplicationContext();
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
