package com.skts.ourmemory.view.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.server.NaverApiDeleteToken;
import com.skts.ourmemory.server.NaverApiMemberProfile;
import com.skts.ourmemory.server.RequestHttpURLConnection;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.signup.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    private LoginPresenter loginPresenter = new LoginPresenter();

    /*카카오*/
    @BindView(R.id.btn_activity_login_kakao_custom_login)
    private LinearLayout mButtonKakaoLogin;             // 카카오 로그인 버튼

    /*구글*/
    @BindView(R.id.btn_activity_login_google_custom_login)
    private LinearLayout mButtonGoogleLogin;            // 구글 로그인 버튼
    private static final int RC_SIGN_IN = 900;          // 구글로그인 result 상수
    private GoogleSignInClient mGoogleSignInClient;     // 구글 api 클라이언트
    private FirebaseAuth mFirebaseAuth;                 // 파이어베이스 인증 객체 생성

    /*네이버*/
    @BindView(R.id.btn_activity_login_naver_custom_login)
    private LinearLayout mButtonNaverLogin;             // 네이버 로그인 버튼
    private OAuthLogin mOAuthLogin;
    private NaverApiMemberProfile mNaverApiMemberProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // presenter 와 연결
        loginPresenter.setView(this);

        // 카카오
        mSession = Session.getCurrentSession();
        mKakaoSessionCallback = new LoginActivity.KakaoSessionCallback();
        mSession.addCallback(mKakaoSessionCallback);
        //mSession.checkAndImplicitOpen();        //자동 로그인

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
        mSession.removeCallback(mKakaoSessionCallback);

        // presenter 와의 연결을 해제합니다.
        loginPresenter.releaseView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달

        if (mSession.handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

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

        super.onActivityResult(requestCode, resultCode, data);
    }

    // 사용자가 정상적으로 로그인한 후에 GoogleSignInAccount 개체에서 ID 토큰을 가져와서
    // Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase 에 인증합니다.
    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        DebugLog.i(TAG, "구글 로그인 성공");

                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                        String id = firebaseUser.getUid();
                        String name = firebaseUser.getDisplayName();
                        String birthday = null;
                        int loginType = 2;

                        passToActivity(id, name, birthday, loginType);
                    } else {
                        // 로그인 실패
                        Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                    }

                });
    }

    // 네이버
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

                mNaverApiMemberProfile = new NaverApiMemberProfile(context, mOAuthLogin, accessToken);
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
                Context context = getApplicationContext();
                String errorCode = mOAuthLogin.getLastErrorCode(context).getCode();
                String errorDesc = mOAuthLogin.getLastErrorDesc(context);

                DebugLog.e(TAG, "errorCode: " + errorCode + ", errorDesc: " + errorDesc);
                Toast.makeText(context, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void processAuthResult(StringBuffer response) {

        try {
            // response 는 json 데이터
            String jSonBody = response.toString();
            DebugLog.d(TAG, jSonBody);
            JSONObject jsonObject = new JSONObject(jSonBody);

            if (jsonObject.getString(ServerConst.NAVER_RESULT_CODE).equals(ServerConst.NAVER_HTTP_ERROR_CODE_401)) {
                DebugLog.e(TAG, "인증에 실패했습니다.");
            } else if (jsonObject.getString(ServerConst.NAVER_RESULT_CODE).equals(ServerConst.NAVER_HTTP_ERROR_CODE_402)) {
                DebugLog.e(TAG, "OAuth 인증 헤더(authorization header)가 없습니다.");
            } else if (jsonObject.getString(ServerConst.NAVER_RESULT_CODE).equals(ServerConst.NAVER_HTTP_ERROR_CODE_403)) {
                DebugLog.e(TAG, "호출 권한이 없습니다. API 요청 헤더에 클라이언트 ID와 Secret 값을 정확히 전송했는지 확인해보시길 바랍니다.");
            } else if (jsonObject.getString(ServerConst.NAVER_RESULT_CODE).equals(ServerConst.NAVER_HTTP_ERROR_CODE_404)) {
                DebugLog.e(TAG, "검색 결과가 없습니다.");
            } else if (jsonObject.getString(ServerConst.NAVER_RESULT_CODE).equals(ServerConst.NAVER_HTTP_ERROR_CODE_405)) {
                DebugLog.e(TAG, "데이터베이스 오류입니다. 서버 내부 에러가 발생하였습니다. 포럼에 올려주시면 신속히 조치하겠습니다.");
            } else {
                // 로그인 성공
                DebugLog.i(TAG, "네이버 로그인 성공");

                JSONObject innerJson = new JSONObject(jsonObject.get(ServerConst.NAVER_RESPONSE).toString());
                String id = null;
                String name = null;
                //String email = null;
                //String nickname = null;
                //String profileImgUrl = null;
                //String gender = null;
                String birthday = null;
                //String age = null;
                //String mobile = null;
                int loginType = 3;

                //필수 정보
                if (!innerJson.has("id")) {
                    DebugLog.e(TAG, "사용자가 아이디 제공을 거부했습니다.");
                } else {
                    id = innerJson.getString("id");
                }

                //필수 정보
                if (!innerJson.has("name")) {
                    DebugLog.e(TAG, "사용자가 이름 제공을 거부했습니다.");
                } else {
                    name = innerJson.getString("name");
                }

                //없는 정보
                /*if (!innerJson.has("email")) {
                    DebugLog.e(TAG, "사용자가 이메일 제공을 거부했습니다.");
                } else {
                    email = innerJson.getString("email");
                }*/

                //없는 정보
                /*if (!innerJson.has("nickname")) {
                    DebugLog.e(TAG, "사용자가 별명 제공을 거부했습니다.");
                } else {
                    nickname = innerJson.getString("nickname");
                }*/

                //없는 정보
                /*if (!innerJson.has("profile_image")) {
                    DebugLog.e(TAG, "사용자가 프로필 사진 제공을 거부했습니다.");
                } else {
                    profileImgUrl = innerJson.getString("profile_image");
                }*/

                //없는 정보
                /*if (!innerJson.has("gender")) {
                    DebugLog.e(TAG, "사용자가 성별 제공을 거부했습니다.");
                } else {
                    gender = innerJson.getString("gender");
                }*/

                //추가 정보
                if (!innerJson.has("birthday")) {
                    DebugLog.e(TAG, "사용자가 생일 제공을 거부했습니다.");
                } else {
                    birthday = innerJson.getString("birthday");
                }

                //없는 정보
                /*if (!innerJson.has("age")) {
                    DebugLog.e(TAG, "사용자가 나이 제공을 거부했습니다.");
                } else {
                    age = innerJson.getString("age");
                }*/

                //없는 정보
                /*if (!innerJson.has("mobile")) {
                    DebugLog.e(TAG, "사용자가 핸드폰 번호 제공을 거부했습니다.");
                } else {
                    mobile = innerJson.getString("mobile");
                }*/

                passToActivity(id, name, birthday, loginType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 카카오 로그아웃 처리
    public void kakaoLogout(View v) {
        /*UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });*/
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

        /*NetworkTask networkTask = new NetworkTask(url, jsonObject.toString());
        networkTask.execute();*/
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
    }

    private void passToActivity(String id, String name, String birthday, int loginType) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra(Const.USER_ID, id);
        intent.putExtra(Const.USER_NAME, name);
        intent.putExtra(Const.USER_BIRTHDAY, birthday);
        intent.putExtra(Const.USER_LOGIN_TYPE, loginType);
        startActivity(intent);
    }
}
