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
import com.skts.ourmemory.model.friend.Friend;

import java.util.ArrayList;
import java.util.List;

public class AddRoomAdapter extends RecyclerView.Adapter<AddRoomAdapter.ViewHolder> {
    private final ArrayList<Friend> mFriendData;
    private OnItemClickListener mOnItemClickListener = null;
    private OnClickListener mOnClickListener = null;
    private int mCheckCount;

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

    // 생성자에서 데이터 리스트 객체를 전달받음
    public AddRoomAdapter(ArrayList<Friend> friendData) {
        mFriendData = friendData;
        mCheckCount = 0;
    }

    public int getCheckCount() {
        return mCheckCount;
    }

    public void setCheckCount(int count) {
        this.mCheckCount += count;
    }

    /**
     * 아이템 뷰를 위한 뷰홀더 객체 생성해 리턴
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.activity_add_room_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = mFriendData.get(position);
        String profile = friend.getProfile();
        if (friend.isSelectStatus()) {
            holder.selectFriendButton.setBackgroundResource(R.drawable.ic_baseline_task_alt_30);
        } else {
            holder.selectFriendButton.setBackgroundResource(R.drawable.ic_outline_circle_30);
        }

        //holder.profileImage
        holder.userName.setText(friend.getName());
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mFriendData.size();
    }

    public Friend getItem(int position) {
        return mFriendData.get(position);
    }

    public ArrayList<Integer> getSelectedFriendIdList() {
        ArrayList<Integer> friendIdList = new ArrayList<>();
        for (int i = 0; i < mFriendData.size(); i++) {
            if (mFriendData.get(i).isSelectStatus()) {
                friendIdList.add(mFriendData.get(i).getFriendId());
            }
        }
        return friendIdList;
    }

    public int getSelectCount() {
        int count = 0;
        for (int i = 0; i < mFriendData.size(); i++) {
            if (mFriendData.get(i).isSelectStatus()) {
                count++;
            }
        }
        return count;
    }

    public List<Object> getShareList() {
        List<Object> list = new ArrayList<>();
        for (Friend friend : mFriendData) {
            if (friend.isSelectStatus()) {
                list.add(friend.getFriendId());
                list.add(friend.getName());
            }
        }
        return list;
    }

    public void setNotifyDataSetChanged() {
        this.notifyDataSetChanged();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        ImageView selectFriendButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.iv_add_room_recyclerview_profile_image);
            this.userName = itemView.findViewById(R.id.tv_add_room_recyclerview_name);
            this.selectFriendButton = itemView.findViewById(R.id.iv_add_room_recyclerview_check_button);

            // 리사이클러뷰 클릭
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });

            // 친추 선택 버튼 클릭
            selectFriendButton.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnClickListener.onClick(view, pos);
                }
            });
        }
    }
}
