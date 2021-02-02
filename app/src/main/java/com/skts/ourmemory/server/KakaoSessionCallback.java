package com.skts.ourmemory.server;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;
import com.skts.ourmemory.util.DebugLog;

public class KakaoSessionCallback implements ISessionCallback {

    private final String TAG = KakaoSessionCallback.class.getSimpleName();

    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        requestMe();
    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        DebugLog.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
    }

    // 사용자 정보 요청
    public void requestMe() {
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

                            passToActivity(id, name, birthday, loginType);
                        }
                    }
                });
    }
}
