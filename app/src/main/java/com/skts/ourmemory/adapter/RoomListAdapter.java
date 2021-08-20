package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.user.UserDAO;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> implements ItemTouchHelperListener {
    private final List<RoomPostResult.ResponseValue> mData;
    private RecyclerView mRecyclerView;

    private OnItemClickListener mOnItemClickListener = null;

    public RoomListAdapter() {
        mData = new ArrayList<>();
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public RoomListAdapter(List<RoomPostResult.ResponseValue> list) {
        mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.recyclerview_fragment_our_memory_list_item, parent, false);

        return new RoomListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomPostResult.ResponseValue responseValue = mData.get(position);
        List<UserDAO> userDAOS = responseValue.getMemberList();
        StringBuilder participants = new StringBuilder();
        participants.append(userDAOS.get(0).getName());
        for (int i = 1; i < userDAOS.size(); i++) {
            participants.append(", ").append(userDAOS.get(i).getName());
        }

        holder.roomTitleTv.setText(responseValue.getName());
        holder.roomParticipantsTv.setText(participants.toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // 이동할 객체 저장
        RoomPostResult.ResponseValue responseValue = mData.get(fromPosition);
        // 이동할 객체 삭제
        mData.remove(fromPosition);
        // 이동하고 싶은 position 에 추가
        mData.add(toPosition, responseValue);

        // Adapter 에 데이터 이동알림
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        // 화면 상단 고정
        // 이동할 객체 저장
        RoomPostResult.ResponseValue responseValue = mData.get(position);
        // 이동할 객체 삭제
        mData.remove(position);
        // 이동하고 싶은 position 에 추가
        mData.add(0, responseValue);

        notifyItemMoved(position, 0);

        // 포커스 이동
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        // 아이템 삭제
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemSelectedChange(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackground(null);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setRecycler(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_our_memory_room_title)
        TextView roomTitleTv;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_our_memory_room_participants)
        TextView roomParticipantsTv;

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
