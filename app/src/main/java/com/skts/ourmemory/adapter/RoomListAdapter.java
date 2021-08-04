package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.user.UserDAO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private final List<RoomPostResult.ResponseValue> mData;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_our_memory_room_title)
        TextView roomTitleTv;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_our_memory_room_participants)
        TextView roomParticipantsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

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
}
