package com.skts.ourmemory.contract;

public class NameContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
