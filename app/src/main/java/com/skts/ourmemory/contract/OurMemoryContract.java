package com.skts.ourmemory.contract;

import android.content.Context;

public class OurMemoryContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void initView(android.view.View view);                      // 초기 뷰 설정
        void initSet();                                             // 초기 설정
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
