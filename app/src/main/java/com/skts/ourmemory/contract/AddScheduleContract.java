package com.skts.ourmemory.contract;

import com.skts.ourmemory.BaseContract;

public class AddScheduleContract {

    public interface View extends BaseContract.View {
        void initDateView();        // 초기 날짜 설정
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        @Override
        boolean isDuplicate();

        String[] initDate();          // 날짜 계산
    }
}
