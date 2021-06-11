package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public TextView textView;
    public RecyclerView recyclerView;
    private CalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;

    private LinearLayout mTotalLayout;
    private LinearLayout mDescriptionLayout;
    int mFirstTouchY;
    int mMoveHeight;
    int mDistance;
    int result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tab, container, false);
        initView(rootView);
        initSet();
        setRecycler(container.getContext());
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initView(View view) {
        textView = view.findViewById(R.id.tv_fragment_tab_date);
        recyclerView = view.findViewById(R.id.rv_fragment_tab_calendar);
        mTotalLayout = view.findViewById(R.id.ll_fragment_tab_total_layout);
        mTotalLayout.setClickable(true);
        mDescriptionLayout = view.findViewById(R.id.ll_fragment_tab_layout);

        mTotalLayout.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstTouchY = (int) motionEvent.getY();
                    mMoveHeight = mDescriptionLayout.getHeight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mDistance = (int) (mFirstTouchY - motionEvent.getY());
                    result = mMoveHeight + mDistance;
                    if (result <= 0) {
                        result = 0;
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
                    mDescriptionLayout.setLayoutParams(params);

                    //DebugLog.e("testtt", ""+mDistance);

                    if (mAdapter != null) {
                        //int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -(mDistance/32), getResources().getDisplayMetrics());
                        //mAdapter.setCalendarHeight(width);
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
        GregorianCalendar calendar = new GregorianCalendar();
        setCalendarList(calendar);
    }

    private void setRecycler(Context context) {
        if (mCalendarList == null) {
            DebugLog.e(TAG, "NO Query, not initializing RecyclerView");
        }

        manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new CalendarAdapter(mCalendarList, context);

        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            recyclerView.scrollToPosition(mCenterPosition);
        }
    }

    public void setCalendarList(GregorianCalendar cal) {
        String date = cal.get(Calendar.YEAR) + "." + (int) (cal.get(Calendar.MONTH) + 1);
        textView.setText(date);
        ArrayList<Object> calendarList = new ArrayList<>();

        //for (int i = -2; i <= 2; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
                //if (i == 0) {
                mCenterPosition = calendarList.size();
                //}

                // Title
                calendarList.add(calendar.getTimeInMillis());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;     // 해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있다
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, (Calendar.MONTH+3), calendar1.getActualMaximum(Calendar.DATE));
                DebugLog.e("testtt", "마지막 주: " + calendar1.get(Calendar.WEEK_OF_MONTH));
                DebugLog.e("testtt", "마지막 날짜!: " + calendar1.get(Calendar.DAY_OF_MONTH));

                // EMPTY 생성
                for (int j = 0; j < dayOfWeek; j++) {
                    calendarList.add(Keys.EMPTY);
                }
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                }

                // TODO : 결과값 넣을 때 여기다가 하면 될 듯
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}

        mCalendarList = calendarList;
    }
}
