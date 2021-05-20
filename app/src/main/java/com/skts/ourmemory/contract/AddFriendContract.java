package com.skts.ourmemory.contract;

public class AddFriendContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void setInitFragment();
        void switchFragment(int index);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
