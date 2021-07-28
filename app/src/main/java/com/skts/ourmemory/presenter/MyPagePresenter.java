package com.skts.ourmemory.presenter;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.skts.ourmemory.api.NaverApiDeleteToken;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.MyPageContract;
import com.skts.ourmemory.model.main.MyPageModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MyPagePresenter implements MyPageContract.Presenter {
    private MyPageContract.Model mModel;
    private MyPageContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MyPagePresenter() {
        mModel = new MyPageModel(this);
    }

    @Override
    public MySharedPreferences getMySharedPreferences() {
        return mMySharedPreferences;
    }

    @Override
    public void setView(MyPageContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }

    @Override
    public void setLogout() {
        // 토큰 삭제
        int loginType = mMySharedPreferences.getIntExtra(Const.LOGIN_TYPE);
        if (loginType == 1) {
            // 카카오
            kakaoLogout();
        } else if (loginType == 2) {
            // 구글
            googleLogout();
        } else {
            // 네이버
            naverLogout();
        }

        // 로그인 Type 삭제
        mMySharedPreferences.removePreference(Const.LOGIN_TYPE);
    }

    @Override
    public void kakaoLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(Long result) {
            }
        });
    }

    @Override
    public void googleLogout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void naverLogout() {
        NaverApiDeleteToken naverApiDeleteToken = new NaverApiDeleteToken(mView.getAppContext(), OAuthLogin.getInstance());
        naverApiDeleteToken.execute();
    }
}
