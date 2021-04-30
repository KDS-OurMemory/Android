package com.skts.ourmemory.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.main.HomeRoomData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRoomAdapter extends RecyclerView.Adapter<HomeRoomAdapter.HomeViewHolder> {

    private final List<HomeRoomData> homeRoomDataList = new ArrayList<>();


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_home_recyclerview, parent, false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        HomeRoomData homeRoomData = homeRoomDataList.get(position);

        holder.roomTitleTv.setText(homeRoomData.getRoomTitle());
        holder.roomParticipantsTv.setText(homeRoomData.getRoomParticipants());
    }

    @Override
    public int getItemCount() {
        return homeRoomDataList.size();
    }

    public void addItem(HomeRoomData homeRoomData) {
        homeRoomDataList.add(homeRoomData);
        notifyDataSetChanged();
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
