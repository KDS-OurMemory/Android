package com.skts.ourmemory.view.schedule;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;

public class ScheduleActivity extends BaseActivity implements ScheduleContract.View {
    private final String TAG = ScheduleActivity.class.getSimpleName();

    private SchedulePresenter mSchedulePresenter;

    /*달력*/
    private TextView mDate;     // 년/월 텍스트뷰

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mSchedulePresenter = new SchedulePresenter();

        // presenter 와 연결
        mSchedulePresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // presenter 와의 연결을 해제합니다.
        mSchedulePresenter.releaseView();
    }
}
