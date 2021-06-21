package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private final int REMAINDER = 56 + 56 + 25 + 20;     // 56: 툴바 높이, 56: 네비게이션바 높이, 25: 상태바 높이, 20: 요일 높이

    private List<Object> mCalendarList;
    private int mCalendarHeight;
    private float mAlpha = 1f;
    private Context mContext;
    private boolean layoutClickable;

    private OnItemClickListener mOnItemClickListener = null;

    public CalendarAdapter(List<Object> calendarList, float density, float height, int lastWeek) {
        this.mCalendarList = calendarList;
        mCalendarHeight = (int) ((height - (REMAINDER * density)) / lastWeek);
    }

    public void setCalendarList(List<Object> calendarList) {
        this.mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    public void initCalendarHeight(int calendarHeight) {
        this.mCalendarHeight = calendarHeight;
        notifyDataSetChanged();
    }

    public void setCalendarHeight(int calendarHeight) {
        this.mCalendarHeight = calendarHeight;
        // 투명도 변경
        setAlpha(0);
        notifyDataSetChanged();
        layoutClickable = true;
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
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

    /**
     * 데이터 넣어서 완성시키는 것
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type 의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model 에 데이터들어감.
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
     * 개수 구하기
     */
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
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
        }

        public void bind(ViewModel model) {
            // 일자 값 가져오기
            String day = ((Day) model).getDay();
            // 일자 값 View 에 보이게하기
            itemDay.setText(day);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mCalendarHeight);
            linearLayout.setLayoutParams(params);           // Layout
            calendarLayout.setAlpha(mAlpha);
            dotLayout.setAlpha(1 - mAlpha);

            /*if (높이의 1/10 이면) {

            } else if (높이의 2/10 이면) {

            } else {

            }*/

            if (layoutClickable) {
                /*linearLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_top));
                calendarLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_top));
                dotLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_top));*/
                /*calendarLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_0));
                dotLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_100));*/
                //layoutClickable = false;
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
