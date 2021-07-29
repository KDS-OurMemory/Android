package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.presenter.FriendPresenter;

import java.util.Objects;

import butterknife.BindView;

public class FriendActivity extends BaseActivity implements FriendContract.View {
    private FriendContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_friend)
    Toolbar mToolbar;       // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_friend_request_recyclerview)
    private RecyclerView mRequestRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_friend_recyclerview)
    private RecyclerView mFriendRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_30);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mPresenter = new FriendPresenter();
        mPresenter.setView(this);

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
        mRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRequestRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    }
}
