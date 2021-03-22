package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.ScheduleContract;
import com.skts.ourmemory.model.CalendarModel;
import com.skts.ourmemory.presenter.SchedulePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    //private final List<CalendarModel> mList = new ArrayList<>();
    private final List<String> mList;
    private final LayoutInflater mLayoutInflater;
    private final ScheduleContract.Presenter mSchedulePresenter;

    /**
     * 생성자
     *
     * @param context context
     */
    public GridAdapter(Context context, List<String> list) {
        mList = list;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSchedulePresenter = new SchedulePresenter();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_calendar_gridview, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvTodayGridView = convertView.findViewById(R.id.tv_activity_schedule_today);
            viewHolder.llSchedule1 = convertView.findViewById(R.id.ll_activity_schedule1);
            viewHolder.llSchedule2 = convertView.findViewById(R.id.ll_activity_schedule2);
            viewHolder.llSchedule3 = convertView.findViewById(R.id.ll_activity_schedule3);
            viewHolder.clickStatus = convertView.findViewById(R.id.ll_activity_schedule_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTodayGridView.setText("" + getItem(position));

        // 해당 날짜 텍스트 컬러, 배경 변경
        // 오늘 day 가져옴
        Integer intToday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String today = String.valueOf(intToday);

        if (today.equals(getItem(position))) {
            viewHolder.tvTodayGridView.setTextColor(Color.BLUE);
        }

        //CalendarModel calendarModel = mList.get(position);

        // 클릭 여부
        /*boolean clicked =  calendarModel.isClicked();
        if (clicked) {
            viewHolder.tvTodayGridView.setTextColor(Color.RED);
        } else if (today.equals(getItem(position))) {      // 오늘 day 텍스트 컬러 변경
            viewHolder.tvTodayGridView.setTextColor(Color.BLUE);
        } else {
            viewHolder.tvTodayGridView.setTextColor(Color.BLACK);
        }*/
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTodayGridView;
        LinearLayout llSchedule1;
        LinearLayout llSchedule2;
        LinearLayout llSchedule3;
        LinearLayout clickStatus;
    }
}
