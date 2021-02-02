package com.skts.ourmemory.view.login;

import com.skts.ourmemory.BaseContract;

public class LoginContract {
    // View 와 Presenter 에 관한 내용을 한 눈에 볼 수 있음.

    interface View extends BaseContract.View {
        void showToast(String message);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        @Override
        void setView(View View);

        @Override
        void releaseView();

        // 카카오 로그인
        void loadKakaoApi();
        
        // 구글 로그인
        void loadGoogleApi();
        
        // 네이버 로그인
        void loadNaverApi();
    }
}
