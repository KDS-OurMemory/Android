package com.skts.ourmemory.view.addfriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.QRContract;
import com.skts.ourmemory.presenter.QRPresenter;
import com.skts.ourmemory.view.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class QRFragment extends BaseFragment implements QRContract.View {
    private Unbinder unbinder;
    private final QRContract.Presenter mPresenter;

    public QRFragment() {
        this.mPresenter = new QRPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend_search_by_qr, container, false);

        unbinder = ButterKnife.bind(this, view);
        mPresenter.setView(this);

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_friend_search_by_qr;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
