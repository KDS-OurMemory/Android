package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RoomShareFragment extends BaseFragment {
    private Unbinder unbinder;
    private Context mContext;
    private List<RoomPostResult.ResponseValue> mRoomData;

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
        mContext = container.getContext();

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
        mRoomData = new ArrayList<>();
    }

    public void setRoomData(RoomPostResult roomPostResult) {
        mRoomData = roomPostResult.getResponseValueList();
    }

    public void showRoomData() {
        if (mRoomData.isEmpty()) {
            // 방 목록 없음
            showNoRoom(true);
            return;
        }
        showNoRoom(false);
    }

    public void showNoRoom(boolean status) {
        if (status) {
            mNoRoomText.setVisibility(View.VISIBLE);
        } else {
            mNoRoomText.setVisibility(View.GONE);
        }
    }
}
