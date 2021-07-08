package com.skts.ourmemory.view.main;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.CalendarAdapter;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.model.schedule.AddSchedulePostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.presenter.MyMemoryPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.Keys;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
    private TextView mDescriptionHeaderText;
    private TextView mDescriptionMainText;
    private ScrollView mScrollView;

    private int mFirstTouchY;       // y축 터치값
    private int mFirstTouchY2;      // y축 터치값
    private int mMoveHeight;
    private int result;
    private final float mDensity;
    private final int mLayoutHeight;
    private int mLastWeek = 0;

    public MyMemoryFragment(float density, int height) {
        this.mPresenter = new MyMemoryPresenter();
        this.mDensity = density;
        this.mLayoutHeight = height;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_my_memory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_my_memory, container, false);
        mPresenter.setView(this);

        initView(rootView);
        initSet();
        setRecycler();
        return rootView;
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
        mDateTextView = view.findViewById(R.id.tv_fragment_my_memory_date);
        ImageView leftClickView = view.findViewById(R.id.iv_fragment_my_memory_left_click);
        ImageView rightClickView = view.findViewById(R.id.iv_fragment_my_memory_right_click);
        mRecyclerView = view.findViewById(R.id.rv_fragment_my_memory_calendar);
        mTotalLayout = view.findViewById(R.id.ll_fragment_my_memory_total_layout);
        mDescriptionLayout = view.findViewById(R.id.ll_fragment_my_memory_layout);
        mDescriptionHeaderText = view.findViewById(R.id.tv_fragment_my_memory_description_header);
        mDescriptionMainText = view.findViewById(R.id.tv_fragment_my_memory_description_main);
        ImageView descriptionDown = view.findViewById(R.id.iv_fragment_my_memory_down_image);

        mScrollView = view.findViewById(R.id.sv_fragment_my_memory_scroll);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab_fragment_my_memory_floating_button);

        mScrollView.setOnTouchListener((view1, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mFirstTouchY2 = (int) motionEvent.getRawY();
                    mMoveHeight = mDescriptionLayout.getHeight();
                    break;
                case MotionEvent.ACTION_UP:
                    // ACTION_UP 함수 호출
                    actionUpLayout();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 스크롤바 최상단이고, 위로 드래그 할 경우
                    if (mScrollView.getScrollY() == 0 && mFirstTouchY2 - motionEvent.getRawY() < 0) {
                        // ACTION_MOVE 함수 호출
                        actionMoveLayout(motionEvent.getRawY(), mFirstTouchY2);
                        return true;        // 내부 드래그 안되도록
                    } else {
                        result = 100000;        // 임의의 큰 수
                    }
                    break;
            }
            return false;
        });

        // 플로팅 버튼
        floatingActionButton.setOnClickListener(view1 -> {
            ((MainActivity) Objects.requireNonNull(getActivity())).startAddScheduleActivity();
           // TODO
        });

        // 설명 레이아웃 닫기
        descriptionDown.setOnClickListener(view1 -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), 0);
            mDescriptionLayout.setLayoutParams(params);

            if (mLastWeek != 0) {
                mAdapter.setCalendarHeight(mTotalLayout.getHeight() / mLastWeek);
            }
        });

        leftClickView.setOnClickListener(view1 -> {
            // 1달 마이너스
            GregorianCalendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth() - 1, 1, 0, 0, 0);
            setCalendarList(calendar);
            mPresenter.setYearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
            mAdapter.setCalendarList(mCalendarList);
            mAdapter.notifyDataSetChanged();
        });

        rightClickView.setOnClickListener(view1 -> {
            // 1달 플러스
            GregorianCalendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth() + 1, 1, 0, 0, 0);
            setCalendarList(calendar);
            mPresenter.setYearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
            mAdapter.setCalendarList(mCalendarList);
            mAdapter.notifyDataSetChanged();
        });

        mDateTextView.setOnClickListener(view1 -> {
            DebugLog.e("testtt", "1");
            DebugLog.e("testtt", "2");
        });
    }

    @Override
    public void initSet() {
        initCalendarList();
    }

    @Override
    public void initCalendarList() {
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        mPresenter.setYearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
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

    @SuppressLint("ClickableViewAccessibility")
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
            /*// 중복 클릭 방지
            TODO
            if (mPresenter.isDuplicate()) {
                return;
            }*/

            mDescriptionHeaderText.setText(mAdapter.getCalendarDay(position));

            // 일정 내역 표시
            List<SchedulePostResult.ResponseValue> responseValueList = mAdapter.getCalendarData(position);
            StringBuilder mainText = new StringBuilder();
            for (int i = 0; i < responseValueList.size(); i++) {
                SchedulePostResult.ResponseValue responseValue = responseValueList.get(i);
                mainText.append(responseValue.getName()).append("\n  ").append(responseValue.getStartDate()).append(" - ").append(responseValue.getEndDate()).append("\n\n");
            }
            if (responseValueList.size() == 0) {
                mDescriptionMainText.setText("일정이 없습니다.");
                mDescriptionMainText.setTextColor(Color.GRAY);
            } else {
                mDescriptionMainText.setText(mainText.toString());
                mDescriptionMainText.setTextColor(Color.BLACK);
            }

            if (mDescriptionLayout.getHeight() == 0) {              // 설명 레이아웃이 닫혀있을 경우에만
                int halfHeight = mTotalLayout.getHeight() / 2;

                mAdapter.setLayoutFoldStatus(halfHeight, mLastWeek, position);

                ValueAnimator animator = ValueAnimator.ofInt(0, halfHeight).setDuration(400);     // 절반 높이까지
                animator.addUpdateListener(valueAnimator -> {
                    mDescriptionLayout.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                    mDescriptionLayout.requestLayout();
                });

                AnimatorSet set = new AnimatorSet();
                set.play(animator);
                set.setInterpolator(new AccelerateDecelerateInterpolator());
                set.start();
            } else {
                mAdapter.setClickedDay(position);                       // 날짜 클릭 시
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                /*// 중복 클릭 방지
                TODO
                if (mPresenter.isDuplicate2()) {
                    return false;
                }*/

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstTouchY = (int) e.getY();
                        mMoveHeight = mDescriptionLayout.getHeight();
                        break;
                    case MotionEvent.ACTION_UP:
                        // ACTION_UP 함수 호출
                        actionUpLayout();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // ACTION_MOVE 함수 호출
                        // TODO 여기 수정해야됨. 레이아웃 이동이 바로 적용 되도록
                        DebugLog.e("testtt", "문제!");
                        actionMoveLayout(e.getY(), mFirstTouchY);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    @Override
    public void setCalendarList(GregorianCalendar cal) {
        String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1);
        int today = cal.get(Calendar.DAY_OF_MONTH);

        // 초기 설정
        mDescriptionHeaderText.setText(String.valueOf(today));
        mDateTextView.setText(date);

        ArrayList<Object> calendarList = new ArrayList<>();

        try {
            GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);

            // Title
            //calendarList.add(calendar.getTimeInMillis());                 // 날짜 타입
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;         // 해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있다
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);     // 해당 월에 마지막 요일

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

    @Override
    public void actionUpLayout() {
        int totalHeight2 = mTotalLayout.getHeight();

        if (result < totalHeight2 / 4) {    // 1/4 보다 작을 경우 레이아웃 내리기
            result = 0;
        } else {                            // 1/4 보다 클 경우 레이아웃 올리기
            result = totalHeight2 / 2;
        }
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
        mDescriptionLayout.setLayoutParams(params2);
        mDescriptionLayout.requestLayout();

        int setHeight = totalHeight2 - result;

        if (mLastWeek != 0) {
            mAdapter.setCalendarHeight(setHeight / mLastWeek);
        }
    }

    @Override
    public void actionMoveLayout(float getY, int firstTouch) {
        int totalHeight = mTotalLayout.getHeight();

        result = mMoveHeight + (int) (firstTouch - getY);
        if (result <= 0) {
            result = 0;
        } else if (result > totalHeight / 2) {
            // 절반 이상 못 넘어가도록
            result = totalHeight / 2;
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), result);
        mDescriptionLayout.setLayoutParams(params);
        //mDescriptionLayout.requestLayout();

        int setHeight = totalHeight - mDescriptionLayout.getHeight();

        if (mLastWeek != 0) {
            mAdapter.setCalendarHeight(setHeight / mLastWeek);
        }
    }

    @Override
    public void updateCalendarData(AddSchedulePostResult addSchedulePostResult) {
        if (addSchedulePostResult.getResponse().getName() != null) {
            showToast(addSchedulePostResult.getResponse().getName() + " 일정이 추가되었습니다");
        }

        AddSchedulePostResult.ResponseValue result = addSchedulePostResult.getResponse();
        SchedulePostResult.ResponseValue responseValue = new SchedulePostResult.ResponseValue(
                result.getMemoryId(),
                result.getWriterId(),
                result.getName(),
                result.getContents(),
                result.getPlace(),
                result.getStartDate(),
                result.getEndDate(),
                result.getBgColor(),
                result.getFirstAlarm(),
                result.getSecondAlarm(),
                result.getRegDate(),
                result.getModDate(),
                result.getMembers()
        );
        mAdapter.addPlusItem(responseValue);
    }
}
