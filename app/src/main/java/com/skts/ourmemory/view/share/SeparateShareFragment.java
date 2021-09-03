package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.AddRoomAdapter;
import com.skts.ourmemory.view.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SeparateShareFragment extends BaseFragment {
    private Unbinder unbinder;
    private Context mContext;
    private AddRoomAdapter mAdapter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_share_recyclerview)
    RecyclerView mRecyclerView;

    public SeparateShareFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_separate, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = container.getContext();

        initSet();
        setRecycler();
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_separate;
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

    }

    public void setRecycler() {
        //mAdapter = new AddRoomAdapter();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }
}
