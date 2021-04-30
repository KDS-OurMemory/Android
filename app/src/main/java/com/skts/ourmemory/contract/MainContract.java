package com.skts.ourmemory.contract;

import com.skts.ourmemory.BaseContract;

public class MainContract {

    public interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int id);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View View);

        @Override
        void releaseView();

        @Override
        boolean isDuplicate();
    }
}
