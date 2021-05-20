package com.skts.ourmemory.contract;

public class OurMemoryContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        void startAddFriendActivity();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}