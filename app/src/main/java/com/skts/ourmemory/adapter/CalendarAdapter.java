package com.skts.ourmemory.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
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
import com.skts.ourmemory.model.calendar.CalendarHeader;
import com.skts.ourmemory.model.calendar.Day;
import com.skts.ourmemory.model.calendar.EmptyDay;
import com.skts.ourmemory.model.calendar.ViewModel;

import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;
    private int mCalendarHeight;
    private Context mContext;
    private boolean layoutFoldStatus = false;
    private int mSetHeight;             // 달력 한 줄 레이아웃 높이

    private OnItemClickListener mOnItemClickListener = null;

    public CalendarAdapter(List<Object> calendarList, float density, float height, int lastWeek) {
        this.mCalendarList = calendarList;
        // 56: 툴바 높이, 56: 네비게이션바 높이, 25: 상태바 높이, 20: 요일 높이
        int REMAINDER = 56 + 56 + 25 + 20;
        mCalendarHeight = (int) ((height - (REMAINDER * density)) / lastWeek);
    }

    public void setCalendarList(List<Object> calendarList) {
        this.mCalendarList = calendarList;
    }

    public void setCalendarHeight(int calendarHeight) {
        this.mCalendarHeight = calendarHeight;
        notifyItemRangeChanged(0, mCalendarList.size(), "CHANGE");
        // 투명도 변경
        //setAlpha(0);
    }

    public void setLayoutFoldStatus(int halfHeight, int lastWeek) {
        this.layoutFoldStatus = true;
        mSetHeight = halfHeight / lastWeek;         // 절반의 높이에서 총 주를 나눈다.
        notifyItemRangeChanged(0, mCalendarList.size(), "ANIMATION");
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
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type 의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model 에 데이터들어감.v
                model.setHeader((Long) item);
            }
            // view 에 표시하기
            headerViewHolder.bind(model);
        } else if (viewType == EMPTY_TYPE) {
            // 비어있는 날짜 타입 꾸미기
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
            EmptyDay model = new EmptyDay();
            emptyViewHolder.bind(model);
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
        } else {        // 애니메이션
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

        public void bind(ViewModel model) {
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
            if (!layoutFoldStatus) {
                layoutParams.height = mCalendarHeight;
                linearLayout.setLayoutParams(layoutParams);

                calendarLayout.setAlpha(1);
                dotLayout.setVisibility(View.INVISIBLE);
                dotLayout.setAlpha(0);
            } else {
                layoutParams.height = mSetHeight;
                linearLayout.setLayoutParams(layoutParams);

                calendarLayout.setAlpha(0);
                dotLayout.setVisibility(View.VISIBLE);
                dotLayout.setAlpha(1);
            }
        }

        public void bind(ViewModel model) {
            // 일자 값 가져오기
            String day = ((Day) model).getDay();

            // 오늘 날짜 가져오기
            String today = ((Day) model).getToday();

            // 일자 값 View 에 보이게하기
            itemDay.setText(day);

            if (today.equals(day)) {
                // 오늘 날짜 표시
                itemDay.setBackgroundResource(R.drawable.calendar_today_background);
            } else {
                itemDay.setBackground(null);
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
