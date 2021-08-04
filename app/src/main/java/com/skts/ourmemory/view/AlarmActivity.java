package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.AlarmContract;
import com.skts.ourmemory.presenter.AlarmPresenter;

import java.util.Objects;

import butterknife.BindView;

public class AlarmActivity extends BaseActivity implements AlarmContract.View {
    private AlarmContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_alarm)
    Toolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_friend_request_recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void initSet() {
        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mPresenter = new AlarmPresenter();
        mPresenter.setView(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
