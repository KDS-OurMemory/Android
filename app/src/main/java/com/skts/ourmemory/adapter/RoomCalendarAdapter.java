package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.calendar.Day;
import com.skts.ourmemory.model.calendar.ViewModel;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.room.RoomResponseValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RoomCalendarAdapter extends RecyclerView.Adapter {
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;
    private RoomResponseValue mData;
    private List<MemoryDAO> mCalendarDateList;
    private int mCalendarHeight;
    private int mClickedDay;            // 선택한 날짜

    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public RoomCalendarAdapter(List<Object> calendarList) {
        this.mCalendarList = calendarList;
        this.mCalendarDateList = new ArrayList<>();
    }

    public void setCalendarList(List<Object> calendarList, float density, float height, int lastWeek) {
        this.mCalendarList = calendarList;
        // 56: 툴바 높이, 40: 달력 표시 높이, 25: 상태바 높이, 25: 요일 높이
        final int REMAINDER = 56 + 40 + 25 + 25;
        mCalendarHeight = (int) ((height - (REMAINDER * density)) / lastWeek);
        mClickedDay = -1;           // 초기화

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mCalendarList.get(position);
        if (item instanceof String) {
            return EMPTY_TYPE;      // 비어있는 일자 타입
        } else {
            return DAY_TYPE;        // 일자 타입
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == EMPTY_TYPE) {       // 비어있는 일자 타입
            return new EmptyViewHolder(layoutInflater.inflate(R.layout.item_day_empty, parent, false));
        } else {                            // 일자 타입
            return new DayViewHolder(layoutInflater.inflate(R.layout.room_item_day, parent, false));
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

    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

    public int getCalendarCount() {
        if (mCalendarDateList != null) {
            mCalendarDateList.size();
        }
        return 0;
    }

    public List<MemoryDAO> getItem(int position) {
        Day model = new Day();
        List<MemoryDAO> list = new ArrayList<>();
        for (MemoryDAO memoryDAO : mCalendarDateList) {
            if (getItemViewType(position) == DAY_TYPE) {
                Object item = mCalendarList.get(position);
                if (model.isHasCalendar(memoryDAO.getStartDate(), memoryDAO.getEndDate(), (Calendar) item)) {
                    list.add(memoryDAO);
                }
            }
        }

        return list;
    }

    public void addItem(RoomResponseValue item) {
        mData = item;
        mCalendarDateList = item.getMemoryList();

        notifyDataSetChanged();
    }

    public void addPlusItem(MemoryDAO memoryDAO) {
        mCalendarDateList.add(memoryDAO);

        notifyDataSetChanged();
    }

    public String getCalendarDay(int position) {
        Day model = new Day();
        Object item = mCalendarList.get(position);
        return model.getClickDay((Calendar) item);
    }

    public void setClickedDay(int position) {
        if (getItemViewType(position) == DAY_TYPE) {
            // 클릭한 날이 날짜일 경우만
            mClickedDay = position;

            notifyDataSetChanged();
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
            emptyLinearLayout.getLayoutParams().height = mCalendarHeight / 2;
        }
    }

    private class DayViewHolder extends RecyclerView.ViewHolder {           // 요일 타입 ViewHolder
        TextView itemDay;
        LinearLayout linearLayout;
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
            itemDay = view.findViewById(R.id.tv_room_item_day_text);
            linearLayout = view.findViewById(R.id.ll_room_item_day_layout);

            dotImage1 = view.findViewById(R.id.iv_room_item_day_dot_image1);
            dotImage2 = view.findViewById(R.id.iv_room_item_day_dot_image2);
            dotImage3 = view.findViewById(R.id.iv_room_item_day_dot_image3);
            dotImage4 = view.findViewById(R.id.iv_room_item_day_dot_image4);
            dotImage5 = view.findViewById(R.id.iv_room_item_day_dot_image5);

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

            // 이번 달 가져오기
            String todayMonth = ((Day) model).getTodayMonth();

            // 이번 년도 가져오기
            String todayYear = ((Day) model).getTodayYear();

            // 오늘 날짜 가져오기
            String today = ((Day) model).getToday();

            // 일자 값 View 에 보이게하기
            itemDay.setText(day);

            // 초기화
            itemDay.setBackground(null);
            itemDay.setTextColor(Color.BLACK);
            itemDay.setTypeface(Typeface.DEFAULT);

            linearLayout.getLayoutParams().height = mCalendarHeight / 2;

            if (today.equals(day) && todayMonth.equals(month) && todayYear.equals(year)) {
                // 오늘 날짜 표시
                itemDay.setBackgroundResource(R.drawable.calendar_today_background);
                itemDay.setTypeface(Typeface.DEFAULT_BOLD);
                itemDay.setTextColor(Color.WHITE);
            } else {
                if (mClickedDay != -1) {        // 초기화 값
                    if (getCalendarDay(mClickedDay).equals(day) && (!today.equals(day))) {
                        // 클릭한 날짜
                        itemDay.setBackgroundResource(R.drawable.calendar_click_background);
                    }
                }
            }

            for (MemoryDAO memoryDAO : mCalendarDateList) {
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
                    if (dotImage1.getVisibility() == View.VISIBLE) {
                        if (dotImage2.getVisibility() == View.VISIBLE) {
                            if (dotImage3.getVisibility() == View.VISIBLE) {
                                if (dotImage4.getVisibility() == View.VISIBLE) {
                                    dotImage5.setVisibility(View.VISIBLE);
                                } else {
                                    dotImage4.setVisibility(View.VISIBLE);
                                }
                            } else {
                                dotImage3.setVisibility(View.VISIBLE);
                            }
                        } else {
                            dotImage2.setVisibility(View.VISIBLE);
                        }
                    } else {
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
