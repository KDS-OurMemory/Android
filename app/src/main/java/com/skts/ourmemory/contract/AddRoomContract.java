package com.skts.ourmemory.contract;

import java.util.ArrayList;

public class AddRoomContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void setInitSetting();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
