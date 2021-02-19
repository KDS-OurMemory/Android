package com.skts.ourmemory.contract;

import com.skts.ourmemory.BaseContract;

public class ScheduleContract {

    public interface View extends BaseContract.View {

    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void setInit();

        void setCalendarDate(int month);
    }
}
