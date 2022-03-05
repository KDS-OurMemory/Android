package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.model.friend.FriendDAO;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<FriendDAO> mPersonData;
    private int mUserId;
    private OnItemClickListener mOnItemClickListener = null;
    private OnClickListener mOnClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        Button addFriendButton;
        Button cancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.iv_fragment_add_friend_search_user_data_profile_image);
            this.userName = itemView.findViewById(R.id.tv_fragment_add_friend_search_user_data_text_view);
            this.addFriendButton = itemView.findViewById(R.id.btn_fragment_add_friend_search_user_data_plus_button);
            this.cancelButton = itemView.findViewById(R.id.btn_fragment_add_friend_search_user_data_cancel_button);

            // 리사이클러뷰 클릭
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });

            // 친추 추가 버튼 클릭
            addFriendButton.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnClickListener.onClick(view, pos);
                }
            });
        }
    }

    public UserListAdapter() {
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public UserListAdapter(List<FriendDAO> data, int userId) {
        mPersonData = data;
        mUserId = userId;
    }

    /**
     * 아이템 뷰를 위한 뷰홀더 객체 생성해 리턴
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.fragment_add_friend_search_user_data, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String profile = mPersonData.get(position).getProfileImageUrl();
        String name = mPersonData.get(position).getName();
        int userId = mPersonData.get(position).getFriendId();
        String friendStatus = mPersonData.get(position).getFriendStatus();

        if (profile == null) {
            Glide.with(holder.itemView.getContext()).load(R.drawable.ic_baseline_person_30).override(150, 150).circleCrop().into(holder.profileImage);
        } else {
            Glide.with(holder.itemView.getContext()).load(profile).override(150, 150).circleCrop().into(holder.profileImage);
        }

        holder.userName.setText(name);

        if (mUserId == userId) {
            // 본인
            holder.addFriendButton.setText("본인입니다");
            holder.addFriendButton.setEnabled(false);
            holder.cancelButton.setVisibility(View.GONE);
        } else if (friendStatus == null) {
            holder.addFriendButton.setText("친구 추가");
            holder.addFriendButton.setEnabled(true);
            holder.cancelButton.setVisibility(View.GONE);
        } else if (friendStatus.equals(ServerConst.WAIT)) {
            holder.addFriendButton.setText("친구 요청 중");
            holder.addFriendButton.setEnabled(false);
            holder.cancelButton.setVisibility(View.VISIBLE);
        } else if (friendStatus.equals(ServerConst.REQUESTED_BY)) {
            holder.addFriendButton.setText("친구 승인");
            holder.addFriendButton.setEnabled(true);
            holder.cancelButton.setVisibility(View.GONE);
        } else {
            holder.addFriendButton.setText("친구 차단");
            holder.addFriendButton.setEnabled(false);
            holder.cancelButton.setVisibility(View.VISIBLE);
        }
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

    public FriendDAO getItem(int position) {
        return mPersonData.get(position);
    }
}
