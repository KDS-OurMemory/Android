package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.LoginContract;
import com.skts.ourmemory.model.login.LoginModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class LoginPresenter implements LoginContract.Presenter {
    private final String TAG = LoginPresenter.class.getSimpleName();

    private final LoginContract.Model mModel;
    private static LoginContract.View mView;
    private MySharedPreferences mMySharedPreferences = new MySharedPreferences();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private long mBackPressTime = 0;

    /*카카오*/
    private KakaoSessionCallback mKakaoSessionCallback;
    private Session mSession;

    /*구글*/
    public FirebaseAuth mFirebaseAuth;
    public GoogleSignInClient mGoogleSignInClient;     // 구글 api 클라이언트

    /*네이버*/
    public OAuthLogin mOAuthLogin;

    @Override
    public Session getSession() {
        return mSession;
    }

    @Override
    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    @Override
    public OAuthLogin getOAuthLogin() {
        return mOAuthLogin;
    }

    @Override
    public OAuthLoginHandler getOAuthLoginHandler() {
        return oAuthLoginHandler;
    }

    public LoginPresenter() {
        this.mModel = new LoginModel(this);
    }

    @Override
    public void setView(LoginContract.View view) {
        mView = view;
        this.mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void setKakaoApi() {
        // 카카오
        mSession = Session.getCurrentSession();
        mKakaoSessionCallback = new KakaoSessionCallback();
        mSession.addCallback(mKakaoSessionCallback);
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

                            Profile profile = kakaoAccount.getProfile();            // 프로필
                            String snsId = String.valueOf(result.getId());          // id
                            String name = profile.getNickname();                    // 별명
                            String birthday = kakaoAccount.getBirthday();           // 생일
                            int snsType = 1;

                            checkSignUp(snsId, name, birthday, snsType);        // 회원가입 여부 확인
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
        String snsId = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        int snsType = 2;

        checkSignUp(snsId, name, null, snsType);        // 회원가입 여부 확인
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

                NaverApiMemberProfile NaverApiMemberProfile = new NaverApiMemberProfile(applicationContext, mOAuthLogin, accessToken);
                try {
                    StringBuffer result = NaverApiMemberProfile.execute().get();

                    // 로그인 처리가 완료되면 수행할 로직 작성
                    processAuthResult(result);
                } catch (ExecutionException | InterruptedException e) {
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
                String snsId = null;
                String name = null;
                //String email = null;
                //String nickname = null;
                //String profileImgUrl = null;
                //String gender = null;
                String birthday = null;
                //String age = null;
                //String mobile = null;
                int snsType = 3;

                //필수 정보
                if (!innerJson.has("id")) {
                    DebugLog.e(TAG, "사용자가 아이디 제공을 거부했습니다.");
                } else {
                    snsId = innerJson.getString("id");
                }

                // 필수 정보
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

                checkSignUp(snsId, name, birthday, snsType);        // 회원가입 여부 확인
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

    @Override
    public void setAutoLogin() {
        if (mMySharedPreferences.containCheck(Const.LOGIN_TYPE)) {
            // 자동 로그인 값이 저장되어 있으면
            int autoLoginValue = mMySharedPreferences.getIntExtra(Const.LOGIN_TYPE);
            if (autoLoginValue == 1) {
                // 카카오
                mSession.checkAndImplicitOpen();        //자동 로그인
            } else if (autoLoginValue == 2) {
                // 구글
                mView.startGoogleLogin(mGoogleSignInClient);
            } else {
                // 네이버
                mOAuthLogin.startOauthLoginActivity(mView.getActivity(), oAuthLoginHandler);
            }
        }
    }

    /**
     * 회원가입 여부 확인
     *
     * @param snsId    sns id
     * @param name     user name
     * @param birthday user birthday
     * @param snsType  sns type(1: 카카오, 2: 구글, 3: 네이버)
     */
    @Override
    public void checkSignUp(String snsId, String name, String birthday, int snsType) {
        mMySharedPreferences.putStringExtra(Const.SNS_ID, snsId);
        mMySharedPreferences.putIntExtra(Const.USER_SNS_TYPE, snsType);

        if (name != null) {
            mMySharedPreferences.putStringExtra(Const.USER_NAME, name);
        }
        if (birthday != null) {
            mMySharedPreferences.putStringExtra(Const.USER_BIRTHDAY, birthday);
        }
        mModel.setIntroData(snsId, snsType, mCompositeDisposable);
    }

    /**
     * 서버 통신 실패
     */
    @Override
    public void getLoginResultFail() {
        mView.showToast("로그인 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    /**
     * 서버 통신 성공
     *
     * @param resultCode     결과 코드
     * @param message        메시지
     * @param userId         user id
     * @param name           이름
     * @param birthday       생일
     * @param isSolar        양력 여부
     * @param isBirthdayOpen 생일 공개 여부
     * @param loginType      sns 타입 1: 카카오, 2: 구글, 3: 네이버
     */
    @Override
    public void getLoginResultSuccess(String resultCode, String message, int userId, String name, String birthday, boolean isSolar, boolean isBirthdayOpen, String pushToken, int loginType) {
        if (resultCode.equals(ServerConst.SUCCESS)) {
            // Success
            mMySharedPreferences.putIntExtra(Const.USER_ID, userId);                // id 저장
            mMySharedPreferences.putStringExtra(Const.USER_NAME, name);             // 이름 저장
            mMySharedPreferences.putStringExtra(Const.USER_BIRTHDAY, birthday);     // 생일 저장
            mMySharedPreferences.putBooleanExtra(Const.USER_IS_SOLAR, isSolar);     // 양력 여부 저장
            mMySharedPreferences.putBooleanExtra(Const.USER_IS_BIRTHDAY_OPEN, isBirthdayOpen);      // 생일 공개 여부 저장
            mMySharedPreferences.putIntExtra(Const.LOGIN_TYPE, loginType);            // 로그인 유형 저장

            if (!mMySharedPreferences.containCheck(Const.ALARM_COUNT)) {
                // 저장 값이 없으면
                mMySharedPreferences.putIntExtra(Const.ALARM_COUNT, 0);        // 초기값 0 저장
            }

            if (!mMySharedPreferences.containCheck(Const.FRIEND_REQUEST_COUNT)) {
                // 저장 값이 없으면
                mMySharedPreferences.putIntExtra(Const.FRIEND_REQUEST_COUNT, 0);        // 초기값 0 저장
            }

            // 휴대폰 내 user id 저장 여부 확인
            if (mMySharedPreferences.getIntExtra(Const.USER_ID) == 0) {
                // 없을 시 저장
                mMySharedPreferences.putIntExtra(Const.USER_ID, userId);
            }

            String savedToken = mMySharedPreferences.getStringExtra(ServerConst.FIREBASE_PUSH_TOKEN);
            if (savedToken.equals(pushToken)) {
                // equals server token and firebase refresh token
                mView.startMainActivity();
            } else {
                // server token refresh
                mModel.setPatchData(userId, savedToken, mCompositeDisposable);
            }
        } else if (resultCode.equals(ServerConst.SERVER_ERROR_CODE_U404)) {
            // 비회원
            String snsId = mMySharedPreferences.getStringExtra(Const.SNS_ID);
            String userName = mMySharedPreferences.getStringExtra(Const.USER_NAME);
            String userBirthday = mMySharedPreferences.getStringExtra(Const.USER_BIRTHDAY);
            int snsType = mMySharedPreferences.getIntExtra(Const.USER_SNS_TYPE);
            mView.startSignUpActivity(snsId, userName, userBirthday, snsType);
        } else {
            // fail
            mView.showToast(message);
        }
    }

    /**
     * 패치 실패
     */
    @Override
    public void getPatchResultFail() {
        mView.showToast("토큰 갱신 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    /**
     * 패치 성공
     *
     * @param resultCode Result code
     * @param message    Message
     * @param patchDate  Patch date
     */
    @Override
    public void getPatchResultSuccess(String resultCode, String message, String patchDate) {
        if (resultCode.equals(ServerConst.SUCCESS)) {
            mView.startMainActivity();
        } else {
            mView.showToast(message);
        }
    }

    @Override
    public boolean exitApp() {
        if (System.currentTimeMillis() - mBackPressTime >= 2000) {
            mBackPressTime = System.currentTimeMillis();
            mView.showToast("뒤로 버튼을 한번 더 누르시면 종료됩니다");
            return false;
        } else return System.currentTimeMillis() - mBackPressTime < 2000;
    }
}