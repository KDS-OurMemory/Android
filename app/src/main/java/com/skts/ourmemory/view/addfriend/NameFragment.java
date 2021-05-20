package com.skts.ourmemory.view.addfriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.NameContract;
import com.skts.ourmemory.presenter.NamePresenter;
import com.skts.ourmemory.view.BaseFragment;

public class NameFragment extends BaseFragment implements NameContract.View {
    private final NameContract.Presenter mPresenter;

    public NameFragment() {
        this.mPresenter = new NamePresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter.setView(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_friend_search_by_name;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }
}
