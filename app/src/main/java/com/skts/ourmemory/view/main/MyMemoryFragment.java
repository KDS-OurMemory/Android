package com.skts.ourmemory.view.main;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.CalendarAdapter;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.presenter.MyMemoryPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.Keys;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class MyMemoryFragment extends BaseFragment implements MyMemoryContract.View {
    private final String TAG = MyMemoryFragment.class.getSimpleName();

    private final MyMemoryContract.Presenter mPresenter;

    private ArrayList<Object> mCalendarList = new ArrayList<>();
    private TextView mDateTextView;
    private RecyclerView mRecyclerView;
    private CalendarAdapter mAdapter;

    private LinearLayout mTotalLayout;
    private LinearLayout mDescriptionLayout;
    private int mFirstTouchY;
    private int mMoveHeight;
    private int mDistance;
    private int result;
    private final float mDensity;
    private final int mLayoutHeight;
    private int mLastWeek = 0;

    public MyMemoryFragment(float density, int height) {
        this.mPresenter = new MyMemoryPresenter();
        this.mDensity = density;
        this.mLayoutHeight = height;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab, container, false);
        mPresenter.setView(this);

        initView(rootView);
        initSet();
        setRecycler();
        return rootView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getAppContext() {
        return Objects.requireNonNull(getActivity()).getApplicationContext();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void initView(View view) {
        mDateTextView = view.findViewById(R.id.tv_fragment_tab_date);
        mRecyclerView = view.findViewById(R.id.rv_fragment_tab_calendar);
        mTotalLayout = view.findViewById(R.id.ll_fragment_tab_total_layout);
        mTotalLayout.setClickable(true);
        mDescriptionLayout = view.findViewById(R.id.ll_fragment_tab_layout);

        mTotalLayout.setOnTouchListener((view1, motionEvent) -> {
            int setHeight;

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstTouchY = (int) motionEvent.getY();
                    mMoveHeight = mDescriptionLayout.getHeight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int totalHeight = mTotalLayout.getHeight();

                    mDistance = (int) (mFirstTouchY - motionEvent.getY());
                    result = mMoveHeight + mDistance;
                    if (result <= 0) {
                        result = 0;
                    } else if (result > totalHeight / 2) {
                        // 절반 이상 못 넘어가도록
                        result = totalHeight / 2;
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
                    mDescriptionLayout.setLayoutParams(params);
                    mDescriptionLayout.requestLayout();

                    setHeight = totalHeight - mDescriptionLayout.getHeight();

                    if (mLastWeek != 0) {
                        mAdapter.setCalendarHeight(setHeight / mLastWeek);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    int totalHeight2 = mTotalLayout.getHeight();

                    if (result < totalHeight2 / 4) {    // 1/4 보다 작을 경우 레이아웃 내리기
                        result = 0;
                    } else {                            // 1/4 보다 클 경우 레이아웃 올리기
                        result = totalHeight2 / 2;
                    }
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
                    mDescriptionLayout.setLayoutParams(params2);
                    mDescriptionLayout.requestLayout();

                    setHeight = totalHeight2 - result;

                    if (mLastWeek != 0) {
                        mAdapter.setCalendarHeight(setHeight / mLastWeek);
                    }

                    break;
            }
            return false;
        });
    }

    @Override
    public void initSet() {
        initCalendarList();
    }

    @Override
    public void initCalendarList() {
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        setCalendarList(calendar);
    }

    @Override
    public int getLastWeek(int year, int month) {
        int lastWeek;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        lastWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        return lastWeek;
    }

    @Override
    public void setRecycler() {
        if (mCalendarList == null) {
            DebugLog.e(TAG, "NO Query, not initializing RecyclerView");
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        mLastWeek = getLastWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

        mAdapter = new CalendarAdapter(mCalendarList, mDensity, mLayoutHeight, mLastWeek);
        // 어댑터 할당
        mPresenter.setAdapterModel(mAdapter);
        mPresenter.setAdapterView(mAdapter);

        mAdapter.setCalendarList(mCalendarList);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        // 캘린더 클릭 시
        mAdapter.setOnItemClickListener((view1, position) -> {
            if (mDescriptionLayout.getHeight() == 0) {              // 설명 레이아웃이 닫혀있을 경우에만

                int halfHeight = mTotalLayout.getHeight() / 2;

                mAdapter.setLayoutFoldStatus(halfHeight, mLastWeek);

                ValueAnimator animator = ValueAnimator.ofInt(0, halfHeight).setDuration(400);     // 절반 높이까지
                animator.addUpdateListener(valueAnimator -> {
                    mDescriptionLayout.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                    mDescriptionLayout.requestLayout();
                });

                AnimatorSet set = new AnimatorSet();
                set.play(animator);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();
            }
        });
    }

    @Override
    public void setCalendarList(GregorianCalendar cal) {
        String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1);
        mDateTextView.setText(date);

        ArrayList<Object> calendarList = new ArrayList<>();

        try {
            GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);

            // Title
            calendarList.add(calendar.getTimeInMillis());               // 날짜 타입
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;     // 해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있다
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

            // EMPTY 생성
            for (int j = 0; j < dayOfWeek; j++) {
                calendarList.add(Keys.EMPTY);           // 비어있는 일자 타입
            }
            for (int j = 1; j <= max; j++) {
                calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));      // 일자 타입
            }

            // TODO : 결과값 넣을 때 여기다가 하면 될 듯
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCalendarList = calendarList;
    }
}
