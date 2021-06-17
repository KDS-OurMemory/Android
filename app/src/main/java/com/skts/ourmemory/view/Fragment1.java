package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.CalendarAdapter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.Keys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Fragment1 extends Fragment {
    private final String TAG = Fragment1.class.getSimpleName();

    public int mCenterPosition;
    private long mCurrentTime;
    public ArrayList<Object> mCalendarList = new ArrayList<>();

    public TextView mDateTextView;
    public RecyclerView mRecyclerView;
    private CalendarAdapter mAdapter;

    private LinearLayout mTotalLayout;
    private LinearLayout mDescriptionLayout;
    private int mFirstTouchY;
    private int mMoveHeight;
    private int mDistance;
    private int result;
    private int mLastWeek;
    private float mDensity;
    private int mLayoutHeight;

    public Fragment1(float density, int height) {
        this.mDensity = density;
        this.mLayoutHeight = height;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab, container, false);
        initView(rootView);
        initSet();
        setRecycler(container.getContext());
        return rootView;
    }

    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int height = mTotalLayout.getHeight();
            mTotalLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);      // 제거 안하면 무한루프
            if (mLastWeek == 0) {
                DebugLog.e(TAG, "Last week is wrong calculate");
                mRecyclerView.setVisibility(View.VISIBLE);
                return;
            }
            mAdapter.initCalendarHeight(height / mLastWeek - 20);      // 20은 여분
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void initView(View view) {
        mDateTextView = view.findViewById(R.id.tv_fragment_tab_date);
        mRecyclerView = view.findViewById(R.id.rv_fragment_tab_calendar);
        mTotalLayout = view.findViewById(R.id.ll_fragment_tab_total_layout);
        mTotalLayout.setClickable(true);
        mDescriptionLayout = view.findViewById(R.id.ll_fragment_tab_layout);

        /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
        mDescriptionLayout.setLayoutParams(layoutParams);*/

        int totalHeight = mTotalLayout.getHeight();

        mTotalLayout.setOnTouchListener((view1, motionEvent) -> {
            int testHeight;

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstTouchY = (int) motionEvent.getY();
                    mMoveHeight = mDescriptionLayout.getHeight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mDistance = (int) (mFirstTouchY - motionEvent.getY());
                    result = mMoveHeight + mDistance;
                    //DebugLog.e("testtt", ""+result);
                    if (result <= 0) {
                        result = 0;
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
                    mDescriptionLayout.setLayoutParams(params);

                    testHeight = totalHeight - mDescriptionLayout.getHeight();

                    if (mAdapter != null) {
                        //int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -(mDistance/4), getResources().getDisplayMetrics());
                        //mAdapter.setCalendarHeight(-mDistance / 4);
                        mAdapter.setCalendarHeight(testHeight / 4);
                        if (testHeight > 0) {
                            mAdapter.setAlpha(1f);
                        } else if (testHeight > -100) {
                            mAdapter.setAlpha(0.95f);
                        } else if (testHeight > -150) {
                            mAdapter.setAlpha(0.9f);
                        } else if (testHeight > -200) {
                            mAdapter.setAlpha(0.8f);
                        } else if (testHeight > -250) {
                            mAdapter.setAlpha(0.7f);
                        } else if (testHeight > -300) {
                            mAdapter.setAlpha(0.6f);
                        } else if (testHeight > -350) {
                            mAdapter.setAlpha(0.5f);
                        } else if (testHeight > -400) {
                            mAdapter.setAlpha(0.4f);
                        } else if (testHeight > -450) {
                            mAdapter.setAlpha(0.3f);
                        } else if (testHeight > -500) {
                            mAdapter.setAlpha(0.2f);
                        } else if (testHeight > -550) {
                            mAdapter.setAlpha(0.1f);
                        } else if (testHeight > -600) {
                            mAdapter.setAlpha(0f);
                        }
                        //DebugLog.e("testtt", String.valueOf(testHeight / 4));
                    }
                    break;
            }
            return false;
        });
    }

    public void initSet() {
        initCalendarList();
    }

    public void initCalendarList() {
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        setCalendarList(calendar);
    }

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

    private void setRecycler(Context context) {
        if (mCalendarList == null) {
            DebugLog.e(TAG, "NO Query, not initializing RecyclerView");
        }

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        int lastWeek = getLastWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);


        mAdapter = new CalendarAdapter(mCalendarList, mDensity, mLayoutHeight, lastWeek, context);

        mAdapter.setCalendarList(mCalendarList);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            mRecyclerView.scrollToPosition(mCenterPosition);
        }

        mAdapter.setOnItemClickListener((view1, position) -> {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int heightPixels = metrics.heightPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), heightPixels / 3);
            mDescriptionLayout.setLayoutParams(params);

            mAdapter.setCalendarHeight(-mDistance / 4);
        });
    }

    public void setCalendarList(GregorianCalendar cal) {
        String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1);
        mDateTextView.setText(date);

        ArrayList<Object> calendarList = new ArrayList<>();

        //for (int i = -2; i <= 2; i++) {
        try {
            GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 0, 1, 0, 0, 0);
            //if (i == 0) {
            mCenterPosition = calendarList.size();
            //}

            // Title
            calendarList.add(calendar.getTimeInMillis());               // 날짜 타입
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;     // 해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있다
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

            mLastWeek = getLastWeek(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);

            /*Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.YEAR, 2021);
            calendar2.set(Calendar.MONTH, 4);       // 5월
            int dayOfMonth = calendar2.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DebugLog.e("testtt", "마지막 주 : " + calendar2.get(Calendar.WEEK_OF_MONTH));*/

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
        //}

        mCalendarList = calendarList;
    }
}
