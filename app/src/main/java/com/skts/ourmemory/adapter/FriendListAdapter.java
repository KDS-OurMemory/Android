package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skts.ourmemory.R;
import com.skts.ourmemory.model.friend.FriendDAO;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private final List<FriendDAO> mData;
    private Context mContext;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.iv_recyclerview_activity_friend_list_item_profile_image);
            this.textView = itemView.findViewById(R.id.tv_recyclerview_activity_friend_list_item_text_view);
        }
    }

    public FriendListAdapter() {
        mData = new ArrayList<>();
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public FriendListAdapter(List<FriendDAO> list) {
        mData = list;
    }

    /**
     * 아이템 뷰를 위한 뷰홀더 객체 생성해 리턴
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.recyclerview_activity_friend_list_item, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Glide 로 이미지 표시
        if (mData.get(position).getProfileImageUrl() == null) {
            Glide.with(mContext).load(R.drawable.ic_baseline_person_30).override(150, 150).circleCrop().into(holder.imageView);
        } else {
            Glide.with(mContext).load(mData.get(position).getProfileImageUrl()).override(150, 150).circleCrop().into(holder.imageView);
        }

        holder.textView.setText(mData.get(position).getName());
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(FriendDAO friendDAO) {
        mData.add(friendDAO);
        notifyDataSetChanged();
    }
}
