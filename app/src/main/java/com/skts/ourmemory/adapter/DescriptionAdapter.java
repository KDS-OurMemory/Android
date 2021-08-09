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
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.ViewHolder> {
    private List<SchedulePostResult.ResponseValue> mData;
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
        SchedulePostResult.ResponseValue responseValue = mData.get(position);

        holder.colorImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(responseValue.getBgColor())));
        holder.titleText.setText(responseValue.getName());

        StringBuilder stringBuilder = new StringBuilder();
        String startDate = responseValue.getStartDate();
        String endDate = responseValue.getEndDate();
        String str = stringBuilder.append(startDate).append(" ~ ").append(endDate).toString();
        holder.dateText.setText(str);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(List<SchedulePostResult.ResponseValue> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public SchedulePostResult.ResponseValue getData(int position) {
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
