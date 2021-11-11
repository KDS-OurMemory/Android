package com.skts.ourmemory.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.CalendarAdapterContract;
import com.skts.ourmemory.model.calendar.Day;
import com.skts.ourmemory.model.calendar.ViewModel;
import com.skts.ourmemory.model.memory.MemoryDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter implements CalendarAdapterContract.View, CalendarAdapterContract.Model {
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;
    private final String PAYLOAD_ANIMATION = "ANIMATION";

    private List<MemoryDAO> mDataList;
    private List<Object> mCalendarList;
    private int mPastClickedDay;                // 이전에 선택한 날짜
    private int mClickedDay;                    // 선택한 날짜
    private int mCalendarHeight;
    private Context mContext;
    private boolean layoutFoldStatus = false;
    private int mSetHeight;                     // 달력 한 줄 레이아웃 높이
    private final ViewGroup.LayoutParams mParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private OnItemClickListener mOnItemClickListener = null;

    public CalendarAdapter(List<Object> calendarList) {
        this.mCalendarList = calendarList;
        mDataList = new ArrayList<>();      // 초기화
    }

    public void setCalendarList(List<Object> calendarList, float density, float height, int lastWeek) {
        this.mCalendarList = calendarList;
        // 56: 툴바 높이, 56: 네비게이션바 높이, 25: 상태바 높이, 20: 요일 높이
        final int REMAINDER = 56 + 56 + 25 + 20;
        mCalendarHeight = (int) ((height - (REMAINDER * density)) / lastWeek);
        mPastClickedDay = -1;               // 초기화
        mClickedDay = -1;                   // 초기화
    }

    public void setCalendarHeight(int calendarHeight) {
        this.mSetHeight = calendarHeight;
        notifyItemRangeChanged(0, mCalendarList.size(), "DRAG");
    }

    public void setLayoutFoldStatus(boolean layoutFoldStatus) {
        this.layoutFoldStatus = layoutFoldStatus;
    }

    public boolean isLayoutFoldStatus() {
        return layoutFoldStatus;
    }

    public void initClickedDay(int clickedDay) {
        this.mClickedDay = clickedDay;
    }

    public void setLayoutFoldStatus(int halfHeight, int lastWeek, int position) {
        this.layoutFoldStatus = true;
        mSetHeight = halfHeight / lastWeek;         // 절반의 높이에서 총 주를 나눈다.
        this.mPastClickedDay = mClickedDay;         // 과거 날짜 옮기기
        this.mClickedDay = position;
        notifyItemRangeChanged(0, mCalendarList.size(), PAYLOAD_ANIMATION);
    }

    public void setClickedDay(int position) {
        if (getItemViewType(position) == DAY_TYPE) {
            // 클릭한 날이 날짜일 경우만
            Day model = new Day();
            String today = model.getToday();
            String clickDay = getCalendarDay(position);
            if (!today.equals(clickDay)) {
                // 오늘 날짜랑 다를 경우에만
                //notifyItemChanged(position, PAYLOAD_CLICK);
                notifyDataSetChanged();
            }
            mPastClickedDay = mClickedDay;
            /*if (mPastClickedDay != -1) {
                String pos2 = getCalendarDay(mPastClickedDay);
                if (!today.equals(pos2)) {
                    // 오늘 날짜랑 다를 경우에만
                    // 지우기
                    notifyItemChanged(mPastClickedDay, PAYLOAD_CANCEL);
                }
            }*/
        }

        this.mClickedDay = position;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {      // 뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof String) {
            return EMPTY_TYPE;      // 비어있는 일자 타입
        } else {
            return DAY_TYPE;        // 일자 타입
        }
    }

    /**
     * ViewHolder 생성
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == EMPTY_TYPE) {           // 비어있는 일자 타입
            return new EmptyViewHolder(layoutInflater.inflate(R.layout.item_day_empty, parent, false));
        } else {                                // 일자 타입
            return new DayViewHolder(layoutInflater.inflate(R.layout.item_day, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == EMPTY_TYPE) {
            // 비어있는 날짜 타입 꾸미기
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            emptyViewHolder.bind();
        } else if (viewType == DAY_TYPE) {
            // 일자 타입 꾸미기
            DayViewHolder dayViewHolder = (DayViewHolder) holder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {
                // Model 에 Calendar 값을 넣어서 몇 일인지 데이터 넣기
                model.setCalendar((Calendar) item);
            }

            // Model 의 데이터를 View 에 표현하기
            dayViewHolder.bind(model);
        }
    }

    /**
     * 데이터 넣어서 완성시키는 것
     *
     * @param payloads 애니메이션 효과 있을 경우
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        int viewType = getItemViewType(position);
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            if (payloads.get(0).toString().equals(PAYLOAD_ANIMATION)) {        // 클릭 시, 애니메이션
                if (viewType == EMPTY_TYPE) {
                    EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;

                    ValueAnimator animator = ValueAnimator.ofInt(mSetHeight * 2, mSetHeight).setDuration(400);
                    animator.addUpdateListener(valueAnimator -> {
                        emptyViewHolder.emptyLinearLayout.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                        emptyViewHolder.emptyLinearLayout.requestLayout();
                    });

                    AnimatorSet set = new AnimatorSet();
                    set.play(animator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.start();
                } else if (viewType == DAY_TYPE) {
                    DayViewHolder dayViewHolder = (DayViewHolder) holder;
                    Day model = new Day();
                    String today = model.getToday();
                    // 과거 클릭한 배경이 있을 때
                    if (mPastClickedDay == position) {
                        // 오늘 날짜가 아닐 때
                        if (!getCalendarDay(mPastClickedDay).equals(today)) {
                            dayViewHolder.itemDay.setBackground(null);
                        }
                    }

                    if (mClickedDay == position && !(getCalendarDay(mClickedDay).equals(today))) {      // 클릭 위치와 오늘 날짜와 같지 않으면
                        dayViewHolder.itemDay.setBackgroundResource(R.drawable.calendar_click_background);
                    }
                    ValueAnimator animator = ValueAnimator.ofInt(mSetHeight * 2, mSetHeight).setDuration(400);
                    animator.addUpdateListener(valueAnimator -> {
                        dayViewHolder.calendarLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_0));       // 감추기
                        dayViewHolder.dotLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_100));          // 나타내기

                        dayViewHolder.linearLayout.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                        dayViewHolder.linearLayout.requestLayout();

                        dayViewHolder.calendarLayout.setLayoutParams(mParams);      // 아이템 높이
                    });

                    AnimatorSet set = new AnimatorSet();
                    set.play(animator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // 애니메이션 종료 시
                            dayViewHolder.calendarLayout.setAlpha(0);
                            dayViewHolder.dotLayout.setAlpha(1);
                            super.onAnimationEnd(animation);
                        }
                    });
                    set.start();
                }
            } else {            // DRAG
                if (viewType == EMPTY_TYPE) {
                    EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                    emptyViewHolder.emptyLinearLayout.getLayoutParams().height = mSetHeight;
                } else if (viewType == DAY_TYPE) {
                    DayViewHolder dayViewHolder = (DayViewHolder) holder;
                    dayViewHolder.linearLayout.getLayoutParams().height = mSetHeight;

                    dayViewHolder.calendarLayout.setLayoutParams(mParams);
                    dayViewHolder.calendarLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_100_immediately));          // 나타내기

                    // 투명도 변경
                    if (mSetHeight >= mCalendarHeight - 10) {       // 10 은 여유
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.calendarLayout.setAlpha(1);
                        dayViewHolder.dotLayout.setAlpha(0);
                    } else if (mSetHeight >= mCalendarHeight * 0.75) {
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.calendarLayout.setAlpha(0.5f);
                        dayViewHolder.dotLayout.setAlpha(0.5f);
                    } else {        // * 0.5
                        dayViewHolder.calendarLayout.setVisibility(View.INVISIBLE);
                        dayViewHolder.calendarLayout.setAlpha(0);
                        dayViewHolder.dotLayout.setAlpha(1);
                    }
                }
            }
        }
    }

    /**
     * 개수 구하기
     */
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public int getCalendarCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public void addItems(List<MemoryDAO> items) {
        mDataList = items;
    }

    @Override
    public void addPlusItem(MemoryDAO item) {
        Day day = new Day();
        boolean check = true;

        // 일정 시작 시간 순서로 정렬
        // 일정 시작 시간이 같을 경우 등록 시간 순서로 정렬
        for (int i = mDataList.size() - 1; i >= 0; i--) {
            if (day.compareDay(mDataList.get(i).getStartDate(), item.getStartDate()) <= 0) {
                mDataList.add(i + 1, item);
                check = false;
                break;
            }
        }

        if (check) {
            // 가장 오래된 일정일 경우에
            mDataList.add(0, item);
        }

        notifyDataSetChanged();
    }

    @Override
    public void editItem(MemoryDAO item) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getMemoryId() == item.getMemoryId()) {
                MemoryDAO memoryDAO = mDataList.get(i);
                String regDate = memoryDAO.getRegDate();
                item.setRegDate(regDate);

                // 둘 중에 날짜 값이 하나라도 다르면 삭제 후 추가
                if (!(memoryDAO.getStartDate().equals(item.getStartDate()) || memoryDAO.getEndDate().equals(item.getEndDate()))) {
                    mDataList.remove(i);
                    addPlusItem(item);
                    return;
                }
                // 날짜 변경 없을 시 데이터 내용만 변경
                mDataList.set(i, item);
                break;
            }
        }

        notifyDataSetChanged();

        /*Day day = new Day();
        int startDay = day.getStartDay(item.getStartDate());
        int diffDays = day.calcDays(item.getStartDate(), item.getEndDate());
        int lastDay = day.getLastDay(item.getStartDate());
        int emptyDayCount = mCalendarList.size() - lastDay;          // 빈 날짜 수*/

        // 시작날짜 + 몇일동안 >= 해당 월의 마지막 일수보다 크면
        // ex. 17일 + 20일 >= 31일
        /*if (startDay + diffDays >= lastDay) {
            notifyItemRangeChanged(emptyDayCount - 1 + startDay, mCalendarList.size());
        } else {
            // 해당 일에 해당하는 값 변경
            notifyItemRangeChanged(emptyDayCount - 1 + startDay, diffDays + 1);
        }*/
    }

    @Override
    public void deleteItem(int memoryId) {
        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).getMemoryId() == memoryId) {
                mDataList.remove(i);
                break;
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public String getCalendarDay(int position) {
        Day model = new Day();
        Object item = mCalendarList.get(position);
        return model.getClickDay((Calendar) item);
    }

    @Override
    public List<MemoryDAO> getCalendarData(int position) {
        Day model = new Day();
        List<MemoryDAO> dataList = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            MemoryDAO memoryDAO = mDataList.get(i);
            if (getItemViewType(position) == DAY_TYPE) {
                Object item = mCalendarList.get(position);
                if (model.isHasCalendar(memoryDAO.getStartDate(), memoryDAO.getEndDate(), (Calendar) item)) {
                    dataList.add(memoryDAO);
                }
            }
        }
        return dataList;
    }

    @Override
    public MemoryDAO getData(int position) {
        return mDataList.get(position);
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {         // 비어있는 요일 타입 ViewHolder
        LinearLayout emptyLinearLayout;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void initView(View view) {
            emptyLinearLayout = view.findViewById(R.id.ll_item_day_empty_layout);
        }

        public void bind() {
            if (layoutFoldStatus) {
                // 접혀있을 때에는 절반 높이
                emptyLinearLayout.getLayoutParams().height = mCalendarHeight / 2;
            } else {
                // 안접혀있을 때에는 전체 높이
                emptyLinearLayout.getLayoutParams().height = mCalendarHeight;
            }
        }
    }

    private class DayViewHolder extends RecyclerView.ViewHolder {           // 요일 타입 ViewHolder
        TextView itemDay;
        LinearLayout linearLayout;
        LinearLayout calendarLayout;
        LinearLayout dotLayout;
        LinearLayout calendar1;
        LinearLayout calendar2;
        LinearLayout calendar3;
        LinearLayout calendar4;
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        TextView text5;
        ImageView dotImage1;
        ImageView dotImage2;
        ImageView dotImage3;
        ImageView dotImage4;
        ImageView dotImage5;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            clickView(itemView);
        }

        public void initView(View view) {
            itemDay = view.findViewById(R.id.tv_item_day_text);
            linearLayout = view.findViewById(R.id.ll_item_day_layout);
            calendarLayout = view.findViewById(R.id.ll_item_day_calendar_layout);
            dotLayout = view.findViewById(R.id.ll_item_day_dot_layout);
            calendar1 = view.findViewById(R.id.ll_item_day_calendar1);
            calendar2 = view.findViewById(R.id.ll_item_day_calendar2);
            calendar3 = view.findViewById(R.id.ll_item_day_calendar3);
            calendar4 = view.findViewById(R.id.ll_item_day_calendar4);
            text1 = view.findViewById(R.id.tv_item_day_calendar_text1);
            text2 = view.findViewById(R.id.tv_item_day_calendar_text2);
            text3 = view.findViewById(R.id.tv_item_day_calendar_text3);
            text4 = view.findViewById(R.id.tv_item_day_calendar_text4);
            text5 = view.findViewById(R.id.tv_item_day_calendar_text_etc);

            dotImage1 = view.findViewById(R.id.iv_item_day_dot_image1);
            dotImage2 = view.findViewById(R.id.iv_item_day_dot_image2);
            dotImage3 = view.findViewById(R.id.iv_item_day_dot_image3);
            dotImage4 = view.findViewById(R.id.iv_item_day_dot_image4);
            dotImage5 = view.findViewById(R.id.iv_item_day_dot_image5);

            dotImage1.setBackgroundResource(R.drawable.color_circle_red);
            dotImage2.setBackgroundResource(R.drawable.color_circle_red);
            dotImage3.setBackgroundResource(R.drawable.color_circle_red);
            dotImage4.setBackgroundResource(R.drawable.color_circle_red);
            dotImage5.setBackgroundResource(R.drawable.color_circle_red);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ViewModel model) {
            // 년 가져오기
            String year = ((Day) model).getYear();

            // 월 가져오기
            String month = ((Day) model).getMonth();

            // 일자 값 가져오기
            String day = ((Day) model).getDay();

            // 오늘 날짜 가져오기
            String today = ((Day) model).getToday();

            // 이번 달 가져오기
            String todayMonth = ((Day) model).getTodayMonth();

            // 이번 년도 가져오기
            String todayYear = ((Day) model).getTodayYear();

            // 일자 값 View 에 보이게하기
            itemDay.setText(day);

            // 초기화
            calendar1.setVisibility(View.INVISIBLE);
            calendar2.setVisibility(View.INVISIBLE);
            calendar3.setVisibility(View.INVISIBLE);
            calendar4.setVisibility(View.INVISIBLE);
            dotImage1.setVisibility(View.INVISIBLE);
            dotImage2.setVisibility(View.INVISIBLE);
            dotImage3.setVisibility(View.INVISIBLE);
            dotImage4.setVisibility(View.INVISIBLE);
            dotImage5.setVisibility(View.INVISIBLE);
            text5.setText("");

            if (layoutFoldStatus) {
                // 접혀있을 때에는 절반 높이
                linearLayout.getLayoutParams().height = mCalendarHeight / 2;
                calendarLayout.setAlpha(0);
                dotLayout.setAlpha(1);
            } else {
                // 안접혀있을 때에는 전체 높이
                linearLayout.getLayoutParams().height = mCalendarHeight;
                calendarLayout.setAlpha(1);
                dotLayout.setAlpha(0);
            }

            itemDay.setBackground(null);
            itemDay.setTextColor(Color.BLACK);
            itemDay.setTypeface(Typeface.DEFAULT);

            if (today.equals(day) && todayMonth.equals(month) && todayYear.equals(year)) {
                // 오늘 날짜 표시
                itemDay.setBackgroundResource(R.drawable.calendar_today_background);
                itemDay.setTypeface(Typeface.DEFAULT_BOLD);
                itemDay.setTextColor(Color.WHITE);
            } else {
                if (mClickedDay != -1) {        // 초기화 값
                    if (getCalendarDay(mClickedDay).equals(day)) {
                        // 클릭한 날짜
                        itemDay.setBackgroundResource(R.drawable.calendar_click_background);
                    }
                }
            }

            int addCount = 0;       // 추가 일정 수
            for (int i = 0; i < getCalendarCount(); i++) {
                MemoryDAO memoryDAO = mDataList.get(i);

                // 월 필터링
                String startMonth = ((Day) model).calcMonth(memoryDAO.getStartDate());
                String endMonth = ((Day) model).calcMonth(memoryDAO.getEndDate());
                if (month != null) {
                    // 해당 월의 일정이 아니면
                    if ((startMonth.compareTo(month) < 0 && endMonth.compareTo(month) < 0)
                            || (startMonth.compareTo(month) > 0 && endMonth.compareTo(month) > 0)) {
                        continue;
                    }
                }

                Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

                if (((Day) model).isHasCalendar(memoryDAO.getStartDate(), memoryDAO.getEndDate(), calendar)) {
                    if (calendar1.getVisibility() == View.VISIBLE) {
                        if (calendar2.getVisibility() == View.VISIBLE) {
                            if (calendar3.getVisibility() == View.VISIBLE) {
                                if (calendar4.getVisibility() == View.VISIBLE) {
                                    addCount++;
                                    text5.setText("+" + addCount);
                                    dotImage5.setVisibility(View.VISIBLE);
                                } else {
                                    text4.setText(memoryDAO.getName());
                                    calendar4.setBackgroundColor(Color.parseColor(memoryDAO.getBgColor()));
                                    calendar4.setVisibility(View.VISIBLE);
                                    dotImage4.setVisibility(View.VISIBLE);
                                }
                            } else {
                                text3.setText(memoryDAO.getName());
                                calendar3.setBackgroundColor(Color.parseColor(memoryDAO.getBgColor()));
                                calendar3.setVisibility(View.VISIBLE);
                                dotImage3.setVisibility(View.VISIBLE);
                            }
                        } else {
                            text2.setText(memoryDAO.getName());
                            calendar2.setBackgroundColor(Color.parseColor(memoryDAO.getBgColor()));
                            calendar2.setVisibility(View.VISIBLE);
                            dotImage2.setVisibility(View.VISIBLE);
                        }
                    } else {
                        text1.setText(memoryDAO.getName());
                        calendar1.setBackgroundColor(Color.parseColor(memoryDAO.getBgColor()));
                        calendar1.setVisibility(View.VISIBLE);
                        dotImage1.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        public void clickView(View itemView) {
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });
        }
    }
}
