package com.skts.ourmemory.api;

import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.login.LoginPresenter;

public class KakaoSessionCallback implements ISessionCallback {

    private final String TAG = KakaoSessionCallback.class.getSimpleName();

    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        DebugLog.i(TAG, "onSessionOpened");
        LoginPresenter loginPresenter = new LoginPresenter();
        loginPresenter.loadKakaoApi();
    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        DebugLog.e(TAG, "onSessionOpenFailed : " + exception.getMessage());
    }
}
