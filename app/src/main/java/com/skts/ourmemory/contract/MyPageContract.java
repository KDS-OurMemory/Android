package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.util.MySharedPreferences;

public class MyPageContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showMyPageData(MyPagePostResult myPagePostResult);
        void setMyPageData();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        MySharedPreferences getMySharedPreferences();

        void setLogout();           // 로그아웃 설정

        void kakaoLogout();         // 카카오 로그아웃

        void googleLogout();        // 구글 로그아웃

        void naverLogout();         // 네이버 로그아웃
    }
}
