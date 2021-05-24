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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private final ArrayList<String> mTitle;
    private final ArrayList<String> mParticipants;

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_our_memory_room_list_recyclerview_room_title)
        TextView roomTitleTv;
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.tv_fragment_our_memory_room_list_recyclerview_room_participants)
        TextView roomParticipantsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    public RoomListAdapter(ArrayList<String> titleList, ArrayList<String> participantList) {
        mTitle = titleList;
        mParticipants = participantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.fragment_our_memory_room_list_recyclerview, parent, false);

        return new RoomListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = mTitle.get(position);
        String participants = mParticipants.get(position);

        holder.roomTitleTv.setText(title);
        holder.roomParticipantsTv.setText(participants);
    }

    @Override
    public int getItemCount() {
        return mTitle.size();
    }
}
