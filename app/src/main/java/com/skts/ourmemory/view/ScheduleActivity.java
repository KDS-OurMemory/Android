package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.GridAdapter;
import com.skts.ourmemory.contract.ScheduleContract;
import com.skts.ourmemory.presenter.SchedulePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ScheduleActivity extends BaseActivity implements ScheduleContract.View {
    private final String TAG = ScheduleActivity.class.getSimpleName();

    private SchedulePresenter mSchedulePresenter;
    private GridAdapter mGridAdapter;

    /*달력*/
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_schedule_date)
    TextView mTvDate;                               // 년/월 텍스트뷰

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.gv_activity_schedule_grid_view)
    GridView mGridView;                             // 그리드뷰

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fab_activity_schedule_floating_button)
    public void onClickFloatingBtn() {
        // 플로팅 버튼 클릭
        Intent intent = new Intent(this, AddScheduleActivity.class);
        startActivity(intent);
    }
}