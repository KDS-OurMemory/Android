package com.skts.ourmemory.api;

import android.content.Context;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.skts.ourmemory.view.login.LoginPresenter;

import java.util.concurrent.ExecutionException;

public class TestHandler extends OAuthLoginHandler {

    @Override
    public void run(boolean success) {
        if (success) {
            LoginPresenter loginPresenter = new LoginPresenter();
            OAuthLogin oAuthLogin = loginPresenter.mOAuthLogin;
            Context testContext = loginPresenter.testContext;

            String accessToken = oAuthLogin.getAccessToken(testContext);
            String refreshToken = oAuthLogin.getRefreshToken(testContext);
            long expiresAt = oAuthLogin.getExpiresAt(testContext);
            String tokenType = oAuthLogin.getTokenType(testContext);

            NaverApiMemberProfile mNaverApiMemberProfile;
            mNaverApiMemberProfile = new NaverApiMemberProfile(testContext, oAuthLogin, accessToken);
            try {
                StringBuffer result = mNaverApiMemberProfile.execute().get();

                // 로그인 처리가 완료되면 수행할 로직 작성
                loginPresenter.processAuthResult(result);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {

        }
    }
}
