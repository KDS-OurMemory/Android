package com.skts.ourmemory.view.addroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.AddRoomAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.model.friend.Friend;
import com.skts.ourmemory.presenter.AddRoomPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class AddRoomActivity extends BaseActivity implements AddRoomContract.View {
    private AddRoomPresenter mAddRoomPresenter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_room)
    Toolbar mToolbar;       // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_add_room_recyclerview)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_room_create_button)
    TextView mCreateButton;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_room_select_number)
    TextView mShowSelectNumber;
    private ArrayList<String> mFriendList;
    private AddRoomAdapter mAddRoomAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mFriendList = new ArrayList<>();
        Intent intent = getIntent();
        mFriendList = intent.getStringArrayListExtra(Const.FRIEND_LIST);

        mAddRoomPresenter = new AddRoomPresenter();
        mAddRoomPresenter.setView(this);

        setInitSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddRoomPresenter.releaseView();
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
    public void setInitSetting() {
        // Set layoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        Friend friend = new Friend(1, "", mFriendList.get(0), false);
        ArrayList<Friend> friendArrayList = new ArrayList<>();
        friendArrayList.add(friend);
        mAddRoomAdapter = new AddRoomAdapter(friendArrayList);
        mRecyclerView.setAdapter(mAddRoomAdapter);

        mAddRoomAdapter.setOnItemClickListener((view, position) -> {
            DebugLog.e("testtt", "" + position);
        });

        mAddRoomAdapter.setOnClickListener((view, position) -> {
            if (mAddRoomAdapter.getItem(position).isSelectStatus()) {
                mAddRoomAdapter.getItem(position).setSelectStatus(false);
            } else {
                mAddRoomAdapter.getItem(position).setSelectStatus(true);
            }
            mShowSelectNumber.setText(String.valueOf(mAddRoomAdapter.getSelectCount()));
            mAddRoomAdapter.setNotifyDataSetChanged();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_add_room_close_button)
    void onClickCloseButton() {
        onBackPressed();
    }
}
