package com.skts.ourmemory.contract;

import com.skts.ourmemory.BaseContract;

public class AddScheduleContract {

    public interface View extends BaseContract.View {

    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
