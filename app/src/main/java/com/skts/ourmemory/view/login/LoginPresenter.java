package com.skts.ourmemory.view.login;

import com.kakao.auth.Session;
import com.skts.ourmemory.server.KaKaoSessionCallback;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    /*카카오*/
    private KaKaoSessionCallback mKakaoSessionCallback;
    private Session mSession;

    LoginPresenter() {
    }

    @Override
    public void setView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    @Override
    public void loadKakaoApi() {
    }

    @Override
    public void loadGoogleApi() {
    }

    @Override
    public void loadNaverApi() {

    }
}
