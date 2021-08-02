package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.presenter.FriendPresenter;
import com.skts.ourmemory.view.addfriend.AddFriendActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class FriendActivity extends BaseActivity implements FriendContract.View {
    private FriendContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_friend)
    Toolbar mToolbar;       // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_friend_request_recyclerview)
    RecyclerView mRequestRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_friend_recyclerview)
    RecyclerView mFriendRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_fragment_our_memory_friend_list_edit_text)
    EditText mSearchText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_friend_no_friend_text)
    TextView mNoFriendText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        // 친구 데이터 요청
        mRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRequestRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        // 친구 목록
        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFriendRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    }

    @Override
    public void setRequestAdapter(RequestFriendListAdapter adapter) {
        mRequestRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showNoFriend(boolean status) {
        if (status) {
            mNoFriendText.setVisibility(View.VISIBLE);
        } else {
            mNoFriendText.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_friend_list_add_friend)
    void onClickAddFriendBtn() {
        startActivity(new Intent(this, AddFriendActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_friend_list_search_friend)
    void onClickSearchFriendBtn() {
        mSearchText.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_friend_list_setting)
    void onClickSettingBtn() {
        showToast("친구 목록 설정 버튼");
    }
}
