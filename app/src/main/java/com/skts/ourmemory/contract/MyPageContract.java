package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.util.MySharedPreferences;

public class MyPageContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        MySharedPreferences getMySharedPreferences();

        void setLogout();

        void kakaoLogout();

        void googleLogout();

        void naverLogout();
    }
}
