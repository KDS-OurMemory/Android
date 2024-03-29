package com.skts.ourmemory.contract;

import android.content.Context;

public class AlarmContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        Context getAppContext();

        void showToast(String message);

        void initSet();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정
    }
}
