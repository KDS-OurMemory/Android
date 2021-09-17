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
import com.skts.ourmemory.model.room.Room;

import java.util.ArrayList;

public class AddRoomChatAdapter extends RecyclerView.Adapter<AddRoomChatAdapter.ViewHolder> {
    private final ArrayList<Room> mData;
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
    public AddRoomChatAdapter(ArrayList<Room> roomData) {
        mData = roomData;
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
        Room room = mData.get(position);
        if (room.isSelectStatus()) {
            holder.selectFriendButton.setBackgroundResource(R.drawable.ic_baseline_task_alt_30);
        } else {
            holder.selectFriendButton.setBackgroundResource(R.drawable.ic_outline_circle_30);
        }

        //holder.profileImage
        holder.userName.setText(room.getName());
        holder.participantsCount.setText(String.valueOf(room.getMembers().size()));
    }

    /**
     * @return 전체 데이터 갯수
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Room getItem(int position) {
        return mData.get(position);
    }

    public ArrayList<Integer> getSelectedRoomIdList() {
        ArrayList<Integer> roomIdList = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelectStatus()) {
                roomIdList.add(mData.get(i).getRoomId());
            }
        }
        return roomIdList;
    }

    public int getSelectCount() {
        int count = 0;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelectStatus()) {
                count++;
            }
        }
        return count;
    }

    public void setNotifyDataSetChanged() {
        this.notifyDataSetChanged();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName;
        TextView participantsCount;
        ImageView selectFriendButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.iv_add_room_recyclerview_profile_image);
            this.userName = itemView.findViewById(R.id.tv_add_room_recyclerview_name);
            this.participantsCount = itemView.findViewById(R.id.tv_add_room_recyclerview_participant_count);
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
