package com.skts.ourmemory.view;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.GridAdapter;
import com.skts.ourmemory.contract.ScheduleContract;
import com.skts.ourmemory.presenter.SchedulePresenter;

import butterknife.BindView;

public class ScheduleActivity extends BaseActivity implements ScheduleContract.View {
    private final String TAG = ScheduleActivity.class.getSimpleName();

    private SchedulePresenter mSchedulePresenter;
    private GridAdapter mGridAdapter;

    /*달력*/
    @BindView(R.id.tv_activity_schedule_date)
    TextView mTvDate;                       // 년/월 텍스트뷰
    @BindView(R.id.gv_activity_schedule_grid_view)
    GridView mGridView;                     // 그리드뷰

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mSchedulePresenter = new SchedulePresenter();

        // presenter 와 연결
        mSchedulePresenter.setView(this);

        mTvDate.setText(mSchedulePresenter.currentYearFormat.format(mSchedulePresenter.date) + "." + mSchedulePresenter.currentMonthFormat.format(mSchedulePresenter.date));

        // gridView 요일 표시
        mSchedulePresenter.setInit();

        // setInit 함수 실행 후 실행되어야함. 그래야 mDayList 값이 들어감
        mGridAdapter = new GridAdapter(getApplicationContext(), mSchedulePresenter.mDayList);
        mGridView.setAdapter(mGridAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // presenter 와의 연결을 해제합니다.
        mSchedulePresenter.releaseView();
    }
}
