package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.SelectPerson;

import java.util.ArrayList;

public class ShowSelectAdapter extends RecyclerView.Adapter<ShowSelectAdapter.ViewHolder> {
    private final ArrayList<SelectPerson> mPersonData;
    private OnItemClickListener mOnItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.iv_activity_add_room_header_recyclerview_profile);
            this.userName = itemView.findViewById(R.id.tv_activity_add_room_header_recyclerview_name);

            // 리사이클러뷰 클릭
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });
        }
    }

    public ShowSelectAdapter() {
        mPersonData = new ArrayList<>();
    }

    /**
     * 아이템 뷰를 위한 뷰홀더 객체 생성해 리턴
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.activity_add_room_header_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String profile = mPersonData.get(position).getProfile();
        String name = mPersonData.get(position).getName();

        //holder.profileImage
        holder.userName.setText(name);
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mPersonData.size();
    }

    public void setListClear() {
        if (mPersonData != null) {
            mPersonData.clear();
            notifyDataSetChanged();
        }
    }

    public void addItem(SelectPerson selectPerson) {
        mPersonData.add(selectPerson);
        notifyDataSetChanged();
    }

    public void deleteListItem(int index) {
        for (int i = 0; i < mPersonData.size(); i++) {
            if (mPersonData.get(i).getIndex() == index) {
                mPersonData.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void deleteShowTopListItem(int index) {
        mPersonData.remove(index);
        notifyDataSetChanged();
    }

    public SelectPerson getItem(int position) {
        return mPersonData.get(position);
    }

    public void setNotifyDataSetChanged() {
        this.notifyDataSetChanged();
    }
}
