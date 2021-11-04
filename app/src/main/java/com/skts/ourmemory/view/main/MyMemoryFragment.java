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
import com.skts.ourmemory.adapter.DescriptionAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.MyMemoryContract;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyMemoryFragment extends BaseFragment implements MyMemoryContract.View {
    private final String TAG = MyMemoryFragment.class.getSimpleName();

    private Unbinder unbinder;
    private MyMemoryContract.Presenter mPresenter;
    private ArrayList<Object> mCalendarList = new ArrayList<>();
    private CalendarAdapter mAdapter;
    private DescriptionAdapter mDescriptionAdapter;
    private Context mContext;
    private ImageView mDescriptionDown;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_my_memory_date)
    TextView mDateTextView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_my_memory_calendar)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_fragment_my_memory_total_layout)
    LinearLayout mTotalLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_fragment_my_memory_layout)
    LinearLayout mDescriptionLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_my_memory_description_header)
    TextView mDescriptionHeaderText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_my_memory_description_lunar)
    TextView mDescriptionLunarText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sv_fragment_my_memory_scroll)
    ScrollView mScrollView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_my_memory_description)
    RecyclerView mDescriptionView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_my_memory_no_calendar_text)
    TextView mNoCalendarText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab_fragment_my_memory_floating_button)
    FloatingActionButton mFloatingActionButton;

    private int mFirstTouchY;       // y축 터치값
    private int mFirstTouchY2;      // y축 터치값
    private int mMoveHeight;
    private int result;
    private float mDensity;
    private int mLayoutHeight;
    private int mLastWeek = 0;

    public MyMemoryFragment() {
    }

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
        View view = inflater.inflate(R.layout.fragment_main_my_memory, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = container.getContext();
        mPresenter.setView(this);

        initView(view);
        initSet();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
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
        ImageView leftClickView = view.findViewById(R.id.iv_fragment_my_memory_left_click);
        ImageView rightClickView = view.findViewById(R.id.iv_fragment_my_memory_right_click);
        mDescriptionDown = view.findViewById(R.id.iv_fragment_my_memory_down_image);

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

        // 설명 레이아웃 닫기
        mDescriptionDown.setOnClickListener(view1 -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDescriptionLayout.getWidth(), 0);
            mDescriptionLayout.setLayoutParams(params);

            mAdapter.setLayoutFoldStatus(false);

            if (mLastWeek != 0) {
                mAdapter.setCalendarHeight(mTotalLayout.getHeight() / mLastWeek);
            }
        });

        leftClickView.setOnClickListener(view1 -> {
            mAdapter.initClickedDay(-1);        // 초기화

            // 1달 마이너스
            GregorianCalendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth() - 1, 1, 0, 0, 0);
            setCalendarList(calendar);
            mPresenter.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
            mLastWeek = getLastWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            mAdapter.setCalendarList(mCalendarList, mDensity, mLayoutHeight, mLastWeek);
            mAdapter.notifyDataSetChanged();
        });

        rightClickView.setOnClickListener(view1 -> {
            mAdapter.initClickedDay(-1);        // 초기화

            // 1달 플러스
            GregorianCalendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth() + 1, 1, 0, 0, 0);
            setCalendarList(calendar);
            mPresenter.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
            mLastWeek = getLastWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            mAdapter.setCalendarList(mCalendarList, mDensity, mLayoutHeight, mLastWeek);
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
        setRecycler();
    }

    @Override
    public void initCalendarList() {
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        mPresenter.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        setCalendarList(calendar);
    }

    @Override
    public int getLastWeek(int year, int month) {
        int lastWeek;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
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
        mAdapter = new CalendarAdapter(mCalendarList);
        // 어댑터 할당
        mPresenter.setAdapterModel(mAdapter);
        mPresenter.setAdapterView(mAdapter);

        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        mLastWeek = getLastWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        mAdapter.setCalendarList(mCalendarList, mDensity, mLayoutHeight, mLastWeek);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mDescriptionAdapter = new DescriptionAdapter();

        mDescriptionView.setLayoutManager(new LinearLayoutManager(mContext));
        mDescriptionView.setAdapter(mDescriptionAdapter);

        SchedulePostResult schedulePostResult = ((MainActivity) getActivity()).getScheduleData();
        if (schedulePostResult != null) {
            showScheduleData(((MainActivity) getActivity()).getScheduleData());
        }

        // 캘린더 클릭 시
        mAdapter.setOnItemClickListener((view1, position) -> {
            String calendarDay = mAdapter.getCalendarDay(position);
            mPresenter.setDay(Integer.parseInt(calendarDay));           // 선택한 날 저장
            mDescriptionHeaderText.setText(calendarDay);

            String lunar = mPresenter.convertSolarToLunar();            // 음력 변환
            mDescriptionLunarText.setText(lunar);

            // 일정 내역 표시
            List<SchedulePostResult.ResponseValue> responseValueList = mAdapter.getCalendarData(position);
            if (responseValueList.size() == 0) {
                // 일정 없음
                mNoCalendarText.setVisibility(View.VISIBLE);
                mDescriptionAdapter.clearData();
            } else {
                mNoCalendarText.setVisibility(View.GONE);
                mDescriptionAdapter.addData(responseValueList);
            }

            if (mDescriptionLayout.getHeight() == 0) {              // 설명 레이아웃이 닫혀있을 경우에만
                int halfHeight = mTotalLayout.getHeight() / 2;

                mAdapter.setLayoutFoldStatus(halfHeight, mLastWeek, position);
                mAdapter.setLayoutFoldStatus(true);

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

        // 플로팅 버튼
        mFloatingActionButton.setOnClickListener(view1 -> ((MainActivity) Objects.requireNonNull(getActivity())).startAddScheduleActivity(null, mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay(), Const.CALENDAR_ADD));

        // 일정 클릭 시
        mDescriptionAdapter.setOnItemClickListener((view, position) -> {
            SchedulePostResult.ResponseValue responseValue = mDescriptionAdapter.getData(position);
            ((MainActivity) Objects.requireNonNull(getActivity())).startAddScheduleActivity(responseValue, mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay(), Const.CALENDAR_EDIT);
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
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
            mAdapter.setLayoutFoldStatus(false);
        } else {                            // 1/4 보다 클 경우 레이아웃 올리기
            result = totalHeight2 / 2;
            mAdapter.setLayoutFoldStatus(true);
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

        int setHeight = totalHeight - mDescriptionLayout.getHeight();

        if (mLastWeek != 0) {
            mAdapter.setCalendarHeight(setHeight / mLastWeek);
        }
    }

    @Override
    public void updateCalendarData(SchedulePostResult.ResponseValue responseValue, String mode) {
        //mDescriptionDown.performClick();        // 설명 레이아웃 닫기

        if (mode.equals(Const.CALENDAR_ADD)) {
            showToast(responseValue.getName() + " 일정이 추가되었습니다");
            if (mAdapter.isLayoutFoldStatus()) {
                // 접혀 있을 때
                Calendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay());
                mDescriptionAdapter.addItem(responseValue, calendar);     // 설명 창
            }
            mAdapter.addPlusItem(responseValue);

            // 설명 레이아웃에 일정 여부 있는지 확인
            if (mDescriptionAdapter.getItemCount() == 0) {
                mNoCalendarText.setVisibility(View.VISIBLE);
            } else {
                mNoCalendarText.setVisibility(View.GONE);
            }
        } else if (mode.equals(Const.CALENDAR_EDIT)) {
            showToast(responseValue.getName() + " 일정이 수정되었습니다");
            Calendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay());
            mDescriptionAdapter.editItem(responseValue, calendar);        // 설명 창
            mAdapter.editItem(responseValue);
        } else {
            // 일정 삭제
            showToast(responseValue.getName() + " 일정이 삭제되었습니다");
            mDescriptionAdapter.deleteItem(responseValue.getMemoryId());
            mAdapter.deleteItem(responseValue.getMemoryId());

            if (mDescriptionAdapter.getItemCount() == 0) {
                mNoCalendarText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showScheduleData(SchedulePostResult schedulePostResult) {
        // 어댑터 데이터 삽입
        mAdapter.addItems(schedulePostResult.getResponse());
        mAdapter.notifyAdapter();

        // 초기 일정 내역 표시
        GregorianCalendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), 1);     // 해당 월의 1일이 몇 요일인지
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;     //  빈 날짜

        // 빈 날짜 + 오늘 날짜 - 1
        List<SchedulePostResult.ResponseValue> responseValueList = mAdapter.getCalendarData(dayOfWeek + mPresenter.getDay() - 1);
        if (responseValueList.size() == 0) {
            // 일정 없음
            mNoCalendarText.setVisibility(View.VISIBLE);
            mDescriptionAdapter.clearData();
        } else {
            mNoCalendarText.setVisibility(View.GONE);
            mDescriptionAdapter.addData(responseValueList);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_fragment_main_my_memory_share_button)
    void onClickFriendView() {
        showToast("공유");
    }
}
