package com.skts.ourmemory.view.schedule;

import com.skts.ourmemory.BaseContract;

public class ScheduleContract {

    interface View extends BaseContract.View {

    }

    interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();


    }
}
