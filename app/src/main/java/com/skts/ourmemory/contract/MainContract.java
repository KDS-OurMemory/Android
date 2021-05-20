package com.skts.ourmemory.contract;

public class MainContract {

    public interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int id);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
