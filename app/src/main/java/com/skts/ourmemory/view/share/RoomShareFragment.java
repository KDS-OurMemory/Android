package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.AddRoomChatAdapter;
import com.skts.ourmemory.model.room.Room;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RoomShareFragment extends BaseFragment {
    private Unbinder unbinder;
    private Context mContext;
    private AddRoomChatAdapter mAdapter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_share_room_recyclerview)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_fragment_share_room_search)
    EditText mSearchText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_share_room_no_room_text)
    TextView mNoRoomText;

    public RoomShareFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_room, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = Objects.requireNonNull(container).getContext();

        initSet();
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_room;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void initSet() {
        setRecycler();
        showRoomData(((ShareActivity) Objects.requireNonNull(getActivity())).getRoomData());
    }

    public void setRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showRoomData(RoomPostResult roomPostResult) {
        if (roomPostResult == null) {
            return;
        }

        List<RoomResponseValue> roomData = roomPostResult.getResponseValueList();
        if (roomData.isEmpty()) {
            // 방 목록 없음
            showNoRoom(true);
            return;
        }
        showNoRoom(false);
        ArrayList<Room> roomList = new ArrayList<>();
        for (int i = 0; i < roomData.size(); i++) {
            RoomResponseValue responseValue = roomData.get(i);
            int privateRoomId = ((ShareActivity) Objects.requireNonNull(getActivity())).getPrivateRoomId();
            if (responseValue.getRoomId() != privateRoomId) {
                // 개인방이 아닌 경우에만 추가
                Room room = new Room(
                        responseValue.getRoomId(),
                        responseValue.getOwnerId(),
                        responseValue.getName(),
                        responseValue.getRegDate(),
                        responseValue.isOpened(),
                        responseValue.getMemberList(),
                        false
                );
                roomList.add(room);
            }
        }

        mAdapter = new AddRoomChatAdapter(roomList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, position) -> {
            DebugLog.e("testtt", "gggg");
        });

        mAdapter.setOnClickListener((view, position) -> {
            if (mAdapter.getItem(position).isSelectStatus()) {
                mAdapter.getItem(position).setSelectStatus(false);
                mAdapter.setCheckCount(-1);
            } else {
                mAdapter.getItem(position).setSelectStatus(true);
                mAdapter.setCheckCount(1);
            }
            ((ShareActivity) Objects.requireNonNull(getActivity())).changeCheckBtn(mAdapter.getCheckCount());
            mAdapter.setNotifyDataSetChanged();
        });
    }

    public void showNoRoom(boolean status) {
        if (status) {
            mNoRoomText.setVisibility(View.VISIBLE);
        } else {
            mNoRoomText.setVisibility(View.GONE);
        }
    }
}
