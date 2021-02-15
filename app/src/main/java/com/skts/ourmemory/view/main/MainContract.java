package com.skts.ourmemory.view.main;

import com.skts.ourmemory.BaseContract;

public class MainContract {

    interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int id);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(MainContract.View View);

        @Override
        void releaseView();
    }
}
