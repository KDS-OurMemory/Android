package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.room.RoomData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRoomAdapter extends RecyclerView.Adapter<HomeRoomAdapter.HomeViewHolder> {
    private final List<RoomData> roomDataList = new ArrayList<>();

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_home_recyclerview, parent, false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        RoomData roomData = roomDataList.get(position);

        holder.roomTitleTv.setText(roomData.getRoomTitle());
        holder.roomParticipantsTv.setText(roomData.getRoomParticipants());
    }

    @Override
    public int getItemCount() {
        return roomDataList.size();
    }

    public void addItem(RoomData roomData) {
        roomDataList.add(roomData);
        notifyDataSetChanged();
    }

    public void clearItem() {
        roomDataList.clear();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_main_home_recyclerview_room_title)
        TextView roomTitleTv;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_main_home_recyclerview_room_participants)
        TextView roomParticipantsTv;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
