package com.skts.ourmemory.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.view.BaseFragment;

public class MyPageFragment extends BaseFragment {
    public MyPageFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_my_page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_my_page, container, false);

        initView(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_main_my_page);

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
    }
}
