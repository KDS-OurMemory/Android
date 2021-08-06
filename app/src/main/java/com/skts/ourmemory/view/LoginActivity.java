package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    private final String TAG = LoginActivity.class.getSimpleName();
    private final LoginContract.Presenter mLoginPresenter = new LoginPresenter();

    private MySharedPreferences mMySharedPreferences;

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

        // 자동 로그인
        mLoginPresenter.setAutoLogin();

        mMySharedPreferences = MySharedPreferences.getInstance(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // TODO : 인터넷 연결 조건 확인
                        DebugLog.w(TAG, "Fetching FCM registration token failed" + task.getException());
                        Toast.makeText(this, "파이어베이스 토큰을 받아오는 데 실패했습니다. 다시 시도해주세요.\n 같은 문제가 반복될 경우 관리자에게 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                        finish();       // 파이어베이스 토큰 받아오지 못할 경우 앱 종료
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

    @Override
    public void onBackPressed() {
        if (mLoginPresenter.exitApp()) {
            moveTaskToBack(true);       // 태스크를 백그라운드로 이동
            finishAndRemoveTask();               // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());     // 앱 프로세스 종료
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_login_kakao_custom_login)
    void onClickKakao() {
        if (mMySharedPreferences.containCheck(Const.USER_SNS_TYPE)) {
            // 로그인 유형이 저장되어 있으면(즉, 자동 로그인이 될 수 있는 경우에는) 클릭 못하도록
            return;
        }

        mLoginPresenter.getSession().open(AuthType.KAKAO_LOGIN_ALL, this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_login_google_custom_login)
    void onClickGoogle() {
        if (mMySharedPreferences.containCheck(Const.USER_SNS_TYPE)) {
            // 로그인 유형이 저장되어 있으면(즉, 자동 로그인이 될 수 있는 경우에는) 클릭 못하도록
            return;
        }

        Intent signInIntent = mLoginPresenter.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, ServerConst.RC_SIGN_IN);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_login_naver_custom_login)
    void onClickNaver() {
        if (mMySharedPreferences.containCheck(Const.USER_SNS_TYPE)) {
            // 로그인 유형이 저장되어 있으면(즉, 자동 로그인이 될 수 있는 경우에는) 클릭 못하도록
            return;
        }

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

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void startGoogleLogin(GoogleSignInClient googleSignInClient) {
        startActivityForResult(googleSignInClient.getSignInIntent(), ServerConst.RC_SIGN_IN);
    }
}
