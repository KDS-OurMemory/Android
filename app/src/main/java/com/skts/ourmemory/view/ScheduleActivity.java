package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.GridAdapter;
import com.skts.ourmemory.contract.ScheduleContract;
import com.skts.ourmemory.presenter.SchedulePresenter;
import com.skts.ourmemory.util.DebugLog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class ScheduleActivity extends BaseActivity implements ScheduleContract.View {
    private ScheduleContract.Presenter mSchedulePresenter;
    private GridAdapter mGridAdapter;

    // 달력
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_schedule_date)
    TextView mTvDate;                               // 년/월 텍스트뷰

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.gv_activity_schedule_grid_view)
    GridView mGridView;                             // 그리드뷰

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.supl_activity_schedule_panel_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;     // 슬라이딩 패널 레이아웃

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_schedule_date_text)
    TextView mDateTextView;                         // 클릭 날짜 텍스트 뷰

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_schedule_grid_view_layout)
    LinearLayout mGridLayout;                       // 그리드뷰 레이아웃

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mSchedulePresenter = new SchedulePresenter();

        // Presenter 와 연결
        mSchedulePresenter.setView(this);

        // 폴링 데이터
        mSchedulePresenter.getPollingData();

        mTvDate.setText(mSchedulePresenter.getCurrentYearFormat().format(mSchedulePresenter.getDate()) + "." + mSchedulePresenter.getCurrentMonthFormat().format(mSchedulePresenter.getDate()));

        // GridView 요일 표시
        mSchedulePresenter.setInit();

        // SetInit 함수 실행 후 실행되어야함. 그래야 mDayList 값이 들어감
        mGridAdapter = new GridAdapter(getApplicationContext(), mSchedulePresenter.getDayList());
        mGridView.setAdapter(mGridAdapter);

        // 해당 그리드 뷰 클릭할 경우
        mGridView.setOnItemClickListener((adapterView, view, i, l) -> {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            mDateTextView.setText(mGridAdapter.getItem(i) + ".");
        });

        mSlidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //DebugLog.e("testtt", ""+slideOffset);
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int widthPixels = metrics.widthPixels;
                int heightPixels = metrics.heightPixels;
                int height = (int) (heightPixels - mGridLayout.getHeight() * slideOffset);
                //int heightPixels = (int) (metrics.heightPixels * (1 - slideOffset));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixels, height);
                mGridView.setLayoutParams(params);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //DebugLog.e("testtt", "2");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Presenter 와의 연결을 해제합니다.
        mSchedulePresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fab_activity_schedule_floating_button)
    public void onClickFloatingBtn() {
        // 플로팅 버튼 클릭
        Intent intent = new Intent(this, AddScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getAppContext() {
        return this;
    }
}
