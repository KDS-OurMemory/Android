package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.calendar.Day;
import com.skts.ourmemory.model.memory.MemoryDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {
    private List<MemoryDAO> mData;
    private OnItemClickListener mOnItemClickListener = null;

    public DescriptionAdapter() {
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.recyclerview_fragment_my_memory_description_list_item, parent, false);

        return new DescriptionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemoryDAO memoryDAO = mData.get(position);

        holder.colorImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(memoryDAO.getBgColor())));
        holder.titleText.setText(memoryDAO.getName());

        StringBuilder stringBuilder = new StringBuilder();
        String startDate = memoryDAO.getStartDate();
        String endDate = memoryDAO.getEndDate();
        String str = stringBuilder.append(startDate).append(" ~ ").append(endDate).toString();
        holder.dateText.setText(str);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(List<MemoryDAO> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void addItem(MemoryDAO item, Calendar calendar) {
        Day day = new Day();
        if (!day.isHasCalendar(item.getStartDate(), item.getEndDate(), calendar)) {
            // 해당 일정이 선택한 날짜값 안에 없을 경우
            return;
        }

        boolean check = true;

        // 일정 시작 시간 순서로 정렬
        // 일정 시작 시간이 같을 경우 등록 시간 순서로 정렬
        for (int i = mData.size() - 1; i >= 0; i--) {
            if (day.compareDay(mData.get(i).getStartDate(), item.getStartDate()) <= 0) {
                mData.add(i + 1, item);
                check = false;
                break;
            }
        }

        if (check) {
            // 가장 오래된 일정일 경우에
            mData.add(0, item);
        }

        notifyDataSetChanged();
    }

    public void addPlusItem() {

    }

    public void editItem(MemoryDAO item, Calendar calendar) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getMemoryId() == item.getMemoryId()) {
                MemoryDAO responseValue = mData.get(i);
                String regDate = responseValue.getRegDate();
                item.setRegDate(regDate);

                // 둘 중에 날짜 값이 하나라도 다르면 삭제 후 추가
                if (!(responseValue.getStartDate().equals(item.getStartDate()) || responseValue.getEndDate().equals(item.getEndDate()))) {
                    mData.remove(i);        // 날짜값 안에 없을 경우 삭제
                    addItem(item, calendar);
                } else {
                    // 날짜 값 변경 없을 시 데이터 내용만 변경
                    mData.set(i, item);
                    notifyDataSetChanged();
                }
                break;
            }
        }
    }

    public void deleteItem(int memoryId) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getMemoryId() == memoryId) {
                mData.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public MemoryDAO getData(int position) {
        return mData.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.iv_recyclerview_fragment_my_memory_description_list_item_color)
        ImageView colorImage;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_recyclerview_fragment_my_memory_description_list_item_title)
        TextView titleText;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_recyclerview_fragment_my_memory_description_list_item_date)
        TextView dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clickView(itemView);
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
