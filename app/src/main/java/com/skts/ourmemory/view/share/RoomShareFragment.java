package com.skts.ourmemory.view.share;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skts.ourmemory.R;
import com.skts.ourmemory.view.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RoomShareFragment extends BaseFragment {
    private Unbinder unbinder;
    private Context mContext;

    public RoomShareFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_room, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = container.getContext();
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
}
