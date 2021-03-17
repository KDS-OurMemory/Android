package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

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
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.skts.ourmemory.R;
import com.skts.ourmemory.api.KakaoSessionCallback;
import com.skts.ourmemory.api.NaverApiMemberProfile;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.LoginContract;
import com.skts.ourmemory.util.DebugLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginPresenter implements LoginContract.Presenter {

    private final String TAG = LoginPresenter.class.getSimpleName();

    private static LoginContract.View mView;

    private long mLastClickTime = 0;

    /*카카오*/
    private KakaoSessionCallback mKakaoSessionCallback;
    public Session mSession;

    /*구글*/
    public FirebaseAuth mFirebaseAuth;
    public GoogleSignInClient mGoogleSignInClient;     // 구글 api 클라이언트

    /*네이버*/
    private NaverApiMemberProfile mNaverApiMemberProfile;
    public OAuthLogin mOAuthLogin;

    public LoginPresenter() {
    }

    @Override
    public void setView(LoginContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }

    @Override
    public boolean isDuplicate() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime > 500) {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }

    @Override
    public void setKakaoApi() {
        // 카카오
        mSession = Session.getCurrentSession();
        mKakaoSessionCallback = new KakaoSessionCallback();
        mSession.addCallback(mKakaoSessionCallback);
        mSession.checkAndImplicitOpen();        //자동 로그인
    }

    @Override
    public void loadKakaoApi() {
        UserManagement.getInstance()
                .me(new MeV2ResponseCallback() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        DebugLog.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        DebugLog.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        UserAccount kakaoAccount = result.getKakaoAccount();
                        if (kakaoAccount != null) {
                            // 로그인 성공
                            DebugLog.i(TAG, "카카오 로그인 성공");

                            Profile profile = kakaoAccount.getProfile();        // 프로필
                            String id = String.valueOf(result.getId());         // id
                            String name = profile.getNickname();                // 별명
                            String birthday = kakaoAccount.getBirthday();       // 생일
                            int loginType = 1;

                            mView.startSignUpActivity(id, name, birthday, loginType);
                        }
                    }
                });
    }

    @Override
    public void setGoogleApi(Activity activity) {
        // 구글 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mView.getAppContext().getString(R.string.default_web_client_id))
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
            mView.firebaseAuthWithGoogle(authCredential);
        } catch (ApiException e) {
            DebugLog.e(TAG, "ApiException error : " + e);
        }
    }

    @Override
    public void passToFirebaseData() {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        String id = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        int loginType = 2;
        mView.startSignUpActivity(id, name, null, loginType);
    }

    @Override
    public void setNaverApi() {
        // 네이버
        // 초기화
        mOAuthLogin = OAuthLogin.getInstance();
        mOAuthLogin.init(mView.getAppContext(), mView.getAppContext().getString(R.string.naver_client_id), mView.getAppContext().getString(R.string.naver_client_secret), mView.getAppContext().getString(R.string.naver_client_name));
    }

    @SuppressLint("HandlerLeak")
    public OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Context applicationContext = mView.getAppContext();
                String accessToken = mOAuthLogin.getAccessToken(applicationContext);
                String refreshToken = mOAuthLogin.getRefreshToken(applicationContext);
                long expiresAt = mOAuthLogin.getExpiresAt(applicationContext);
                String tokenType = mOAuthLogin.getTokenType(applicationContext);

                DebugLog.i(TAG, "accessToken : " + accessToken);
                DebugLog.i(TAG, "refreshToken : " + refreshToken);
                DebugLog.i(TAG, "expiresAt : " + expiresAt);
                DebugLog.i(TAG, "tokenType : " + tokenType);

                mNaverApiMemberProfile = new NaverApiMemberProfile(applicationContext, mOAuthLogin, accessToken);
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
                Context applicationContext = mView.getAppContext();
                String errorCode = mOAuthLogin.getLastErrorCode(applicationContext).getCode();
                String errorDesc = mOAuthLogin.getLastErrorDesc(applicationContext);

                DebugLog.e(TAG, "errorCode: " + errorCode + ", errorDesc: " + errorDesc);
                mView.showToast("errorCode:" + errorCode + ", errorDesc:" + errorDesc);
            }
        }
    };

    @Override
    public void processAuthResult(StringBuffer response) {
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
                //mView.showToast("네이버 로그인 성공");

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

                mView.startSignUpActivity(id, name, birthday, loginType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCallback() {
        // 세션 콜백 삭제 (카카오)
        mSession.removeCallback(mKakaoSessionCallback);
    }
}
