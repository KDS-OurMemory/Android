package com.skts.ourmemory.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.presenter.SchedulePresenter;

import java.util.Calendar;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final List<String> mList;
    private final LayoutInflater mLayoutInflater;
    private SchedulePresenter mSchedulePresenter;

    /**
     * 생성자
     *
     * @param context
     * @param list
     */
    public GridAdapter(Context context, List<String> list) {
        this.mList = list;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTodayGridView.setText("" + getItem(position));

        // 해당 날짜 텍스트 컬러, 배경 변경
        mSchedulePresenter.mCalendar = Calendar.getInstance();
        // 오늘 day 가져옴
        Integer intToday = mSchedulePresenter.mCalendar.get(Calendar.DAY_OF_MONTH);
        String today = String.valueOf(intToday);
        if (today.equals(getItem(position))) {      // 오늘 day 텍스트 컬러 변경
            viewHolder.tvTodayGridView.setTextColor(Color.parseColor("#0000ff"));
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvTodayGridView;
        LinearLayout llSchedule1;
        LinearLayout llSchedule2;
        LinearLayout llSchedule3;
    }
}
