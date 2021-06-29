package com.skts.ourmemory.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.CalendarAdapterContract;
import com.skts.ourmemory.model.calendar.CalendarHeader;
import com.skts.ourmemory.model.calendar.Day;
import com.skts.ourmemory.model.calendar.EmptyDay;
import com.skts.ourmemory.model.calendar.ViewModel;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter implements CalendarAdapterContract.View, CalendarAdapterContract.Model {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;
    private final String PAYLOAD_ANIMATION = "ANIMATION";

    private List<SchedulePostResult.ResponseValue> mDataList;
    private List<Object> mCalendarList;
    private int mCalendarHeight;
    private Context mContext;
    private boolean layoutFoldStatus = false;
    private boolean layoutMoveStatus = false;
    private int mSetHeight;             // 달력 한 줄 레이아웃 높이
    private int mTotalHeight;           // 달력 총 높이

    private OnItemClickListener mOnItemClickListener = null;

    public CalendarAdapter(List<Object> calendarList, float density, float height, int lastWeek) {
        this.mCalendarList = calendarList;
        // 56: 툴바 높이, 56: 네비게이션바 높이, 25: 상태바 높이, 20: 요일 높이
        int REMAINDER = 56 + 56 + 25 + 20;
        mTotalHeight = (int) (height - (REMAINDER * density));
        mCalendarHeight = (int) ((height - (REMAINDER * density)) / lastWeek);
    }

    public void setCalendarList(List<Object> calendarList) {
        this.mCalendarList = calendarList;
    }

    public void setCalendarHeight(int calendarHeight) {
        layoutMoveStatus = true;
        this.mSetHeight = calendarHeight;
        notifyItemRangeChanged(0, mCalendarList.size(), "DRAG");
    }

    public void setLayoutFoldStatus(int halfHeight, int lastWeek) {
        this.layoutFoldStatus = true;
        mSetHeight = halfHeight / lastWeek;         // 절반의 높이에서 총 주를 나눈다.
        notifyItemRangeChanged(0, mCalendarList.size(), PAYLOAD_ANIMATION);
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
        if (item instanceof Long) {
            return HEADER_TYPE;
        } else if (item instanceof String) {
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
        if (viewType == HEADER_TYPE) {              // 날짜 타입
            HeaderViewHolder viewHolder = new HeaderViewHolder(layoutInflater.inflate(R.layout.item_calendar_header, parent, false));
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true);               // Span 을 하나로 통합하기
            viewHolder.itemView.setLayoutParams(params);
            return viewHolder;
        } else if (viewType == EMPTY_TYPE) {        // 비어있는 일자 타입
            return new EmptyViewHolder(layoutInflater.inflate(R.layout.item_day_empty, parent, false));
        } else {                                    // 일자 타입
            return new DayViewHolder(layoutInflater.inflate(R.layout.item_day, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == HEADER_TYPE) {
            DebugLog.e("testtt","8");
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type 의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model 에 데이터들어감
                model.setHeader((Long) item);
            }
            // view 에 표시하기
            headerViewHolder.bind(model);
        } else if (viewType == EMPTY_TYPE) {
            DebugLog.e("testtt","7");
            // 비어있는 날짜 타입 꾸미기
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            EmptyDay model = new EmptyDay();
            emptyViewHolder.bind();
        } else if (viewType == DAY_TYPE) {
            DebugLog.e("testtt","6");
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
                    ValueAnimator animator = ValueAnimator.ofInt(mSetHeight * 2, mSetHeight).setDuration(400);
                    animator.addUpdateListener(valueAnimator -> {
                        dayViewHolder.calendarLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_0));       // 감추기
                        dayViewHolder.dotLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_100));          // 나타내기

                        dayViewHolder.linearLayout.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                        dayViewHolder.linearLayout.requestLayout();
                    });

                    AnimatorSet set = new AnimatorSet();
                    set.play(animator);
                    set.setInterpolator(new AccelerateDecelerateInterpolator());
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // 애니메이션 종료 시
                            //layoutFoldStatus = false;
                            dayViewHolder.calendarLayout.setAlpha(0);
                            dayViewHolder.dotLayout.setAlpha(1);
                            super.onAnimationEnd(animation);
                        }
                    });
                    set.start();
                }
            } else {        //      드래그 시
                if (viewType == EMPTY_TYPE) {
                    EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
                    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                    layoutParams.height = mSetHeight;
                    emptyViewHolder.emptyLinearLayout.setLayoutParams(layoutParams);
                } else if (viewType == DAY_TYPE) {
                    DayViewHolder dayViewHolder = (DayViewHolder) holder;
                    ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                    layoutParams.height = mSetHeight;
                    dayViewHolder.linearLayout.setLayoutParams(layoutParams);

                    // 투명도 변경
                    if (mSetHeight <= mTotalHeight / 10) {
                        dayViewHolder.calendarLayout.setAlpha(0);
                        dayViewHolder.calendarLayout.setVisibility(View.INVISIBLE);
                        dayViewHolder.dotLayout.setAlpha(1);
                    } else if (mSetHeight <= mTotalHeight / 9) {
                        dayViewHolder.calendarLayout.setAlpha(0.2f);
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.setAlpha(0.8f);
                    } else if (mSetHeight <= mTotalHeight / 8) {
                        dayViewHolder.calendarLayout.setAlpha(0.4f);
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.setAlpha(0.6f);
                    } else if (mSetHeight <= mTotalHeight / 7) {
                        dayViewHolder.calendarLayout.setAlpha(0.6f);
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.setAlpha(0.4f);
                    } else if (mSetHeight <= mTotalHeight / 6) {
                        dayViewHolder.calendarLayout.setAlpha(0.7f);
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.setAlpha(0.3f);
                    } else if (mSetHeight <= mTotalHeight / 5) {
                        dayViewHolder.calendarLayout.setAlpha(0.9f);
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.setAlpha(0.2f);
                    } else if (mSetHeight <= mTotalHeight / 4) {
                        dayViewHolder.calendarLayout.setAlpha(1);
                        dayViewHolder.calendarLayout.setVisibility(View.VISIBLE);
                        dayViewHolder.dotLayout.setAlpha(0);
                    }

                    /*ViewGroup.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mSetHeight);
                    dayViewHolder.calendarLayout.setLayoutParams(layoutParams2);*/
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
    public void addItems(List<SchedulePostResult.ResponseValue> items) {
        mDataList = items;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void initView(View view) {
            itemHeaderTitle = view.findViewById(R.id.item_header_title);
        }

        public void bind(ViewModel model) {
            // 일자 값 가져오기
            String header = ((CalendarHeader) model).getHeader();

            // Header 에 표시하기
            itemHeaderTitle.setText(header);
        }
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mCalendarHeight);
            emptyLinearLayout.setLayoutParams(params);           // Layout
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

            calendar1.setBackgroundResource(R.color.red);
            calendar2.setBackgroundResource(R.color.yellow);
            calendar3.setBackgroundResource(R.color.teal_700);
            calendar4.setBackgroundResource(R.color.post_it_background5);
            dotImage1.setBackgroundResource(R.drawable.color_circle_red);
            dotImage2.setBackgroundResource(R.drawable.color_circle_red);
            dotImage3.setBackgroundResource(R.drawable.color_circle_red);
            dotImage4.setBackgroundResource(R.drawable.color_circle_red);
            dotImage5.setBackgroundResource(R.drawable.color_circle_red);

            // Init
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (!layoutFoldStatus) {        // 제일 처음 (레이아웃을 접지 않았을 때)
                layoutParams.height = mCalendarHeight;
                linearLayout.setLayoutParams(layoutParams);

                calendarLayout.setAlpha(1);
                dotLayout.setVisibility(View.INVISIBLE);
                dotLayout.setAlpha(0);
                DebugLog.e("testtt","3");
            } else {
                if (!layoutMoveStatus) {
                    DebugLog.e("testtt","2");
                    layoutParams.height = mSetHeight;
                    linearLayout.setLayoutParams(layoutParams);

                    calendarLayout.setAlpha(0);
                    dotLayout.setVisibility(View.VISIBLE);
                    dotLayout.setAlpha(1);
                } else {
                    DebugLog.e("testtt","1");
                }
            }
            DebugLog.e("testtt","4");
        }

        @SuppressLint("SetTextI18n")
        public void bind(ViewModel model) {
            // 월 가져오기
            String month = ((Day) model).getMonth();

            // 일자 값 가져오기
            String day = ((Day) model).getDay();

            // 오늘 날짜 가져오기
            String today = ((Day) model).getToday();

            DebugLog.e("testtt","5");

            // 일자 값 View 에 보이게하기
            itemDay.setText(day);

            if (today.equals(day)) {
                // 오늘 날짜 표시
                itemDay.setBackgroundResource(R.drawable.calendar_today_background);
            } else {
                itemDay.setBackground(null);
            }

            int addCount = 0;       // 추가 일정 수

            for (int i = 0; i < getCalendarCount(); i++) {
                // 월 필터링
                String startMonth = ((Day) model).getStartMonth(mDataList.get(i).getStartDate());
                if (!month.equals(startMonth)) {        // 해당 월의 일정이 아니면
                    continue;
                }

                String startDate = ((Day) model).getStartDay(mDataList.get(i).getStartDate());
                if (startDate.equals(String.valueOf(day))) {
                    if (calendar1.getVisibility() == View.VISIBLE) {
                        if (calendar2.getVisibility() == View.VISIBLE) {
                            if (calendar3.getVisibility() == View.VISIBLE) {
                                if (calendar4.getVisibility() == View.VISIBLE) {
                                    addCount++;
                                    text5.setText("+" + addCount);
                                    dotImage5.setVisibility(View.VISIBLE);
                                } else {
                                    text4.setText(mDataList.get(i).getName());
                                    calendar4.setBackgroundColor(Color.parseColor(mDataList.get(i).getBgColor()));
                                    calendar4.setVisibility(View.VISIBLE);
                                    dotImage4.setVisibility(View.VISIBLE);
                                }
                            } else {
                                text3.setText(mDataList.get(i).getName());
                                calendar3.setBackgroundColor(Color.parseColor(mDataList.get(i).getBgColor()));
                                calendar3.setVisibility(View.VISIBLE);
                                dotImage3.setVisibility(View.VISIBLE);
                            }
                        } else {
                            text2.setText(mDataList.get(i).getName());
                            calendar2.setBackgroundColor(Color.parseColor(mDataList.get(i).getBgColor()));
                            calendar2.setVisibility(View.VISIBLE);
                            dotImage2.setVisibility(View.VISIBLE);
                        }
                    } else {
                        text1.setText(mDataList.get(i).getName());
                        calendar1.setBackgroundColor(Color.parseColor(mDataList.get(i).getBgColor()));
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
