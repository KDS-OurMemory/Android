package com.skts.ourmemory.view.addroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.AddRoomAdapter;
import com.skts.ourmemory.adapter.ShowSelectAdapter;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.model.SelectPerson;
import com.skts.ourmemory.model.friend.Friend;
import com.skts.ourmemory.presenter.AddRoomPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class AddRoomActivity extends BaseActivity implements AddRoomContract.View {
    private AddRoomContract.Presenter mAddRoomPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_room)
    Toolbar mToolbar;       // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_add_room_recyclerview)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_room_select_number)
    TextView mShowSelectNumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_add_room_header_layout)
    LinearLayout mHeaderLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_add_room_header_recyclerview)
    RecyclerView mHeaderRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_room_create_button)
    TextView mCreateButton;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_room_no_friend_text)
    TextView mNoFriendText;

    private ShowSelectAdapter mShowSelectAdapter;

    // Dialog
    AlertDialog mAlertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

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
    public void initSet() {
        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mAddRoomPresenter = new AddRoomPresenter();
        mAddRoomPresenter.setView(this);

        // Set layoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        // Set headerRecycler layoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeaderRecyclerView.setLayoutManager(linearLayoutManager);

        // 버튼 비활성화
        mCreateButton.setEnabled(false);
        mShowSelectNumber.setEnabled(false);
    }

    @Override
    public void setAddRoomAdapter(AddRoomAdapter addRoomAdapter) {
        mRecyclerView.setAdapter(addRoomAdapter);

        mShowSelectAdapter = new ShowSelectAdapter();
        mHeaderRecyclerView.setAdapter(mShowSelectAdapter);

        addRoomAdapter.setOnItemClickListener((view, position) -> DebugLog.e("testtt", "" + position));

        addRoomAdapter.setOnClickListener((view, position) -> {
            if (addRoomAdapter.getItem(position).isSelectStatus()) {
                addRoomAdapter.getItem(position).setSelectStatus(false);
                // TODO
                mShowSelectAdapter.deleteListItem(position);
            } else {
                Friend friend = addRoomAdapter.getItem(position);
                friend.setSelectStatus(true);

                // TODO
                SelectPerson selectPerson = new SelectPerson(friend.getFriendId(), "", friend.getName(), position);
                mShowSelectAdapter.addItem(selectPerson);
            }
            checkCount(addRoomAdapter.getSelectCount());
        });

        mShowSelectAdapter.setOnItemClickListener((view, position) -> {
            int index = mShowSelectAdapter.getItem(position).getIndex();
            mShowSelectAdapter.deleteShowTopListItem(position);
            addRoomAdapter.getItem(index).setSelectStatus(!addRoomAdapter.getItem(index).isSelectStatus());

            checkCount(addRoomAdapter.getSelectCount());
        });
    }

    @Override
    public void showNoFriend(boolean status) {
        if (status) {
            mNoFriendText.setVisibility(View.VISIBLE);
        } else {
            mNoFriendText.setVisibility(View.GONE);
        }
    }

    @Override
    public void checkCount(int count) {
        if (count > 0) {
            mHeaderLayout.setVisibility(View.VISIBLE);
            mCreateButton.setEnabled(true);
            mShowSelectNumber.setEnabled(true);
        } else {
            mHeaderLayout.setVisibility(View.GONE);
            mCreateButton.setEnabled(false);
            mShowSelectNumber.setEnabled(false);
        }
        mShowSelectNumber.setText(String.valueOf(count));

        AddRoomAdapter addRoomAdapter = mAddRoomPresenter.getAddRoomAdapter();
        addRoomAdapter.setNotifyDataSetChanged();
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_add_room_close_button)
    void onClickCloseButton() {
        onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tv_activity_add_room_create_button)
    void onClickCreateButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        mAlertDialog.setTitle("일정 공유방 생성");
        mAlertDialog.setMessage("공유방을 생성하시겠습니까?");
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
            AddRoomAdapter addRoomAdapter = mAddRoomPresenter.getAddRoomAdapter();
            ArrayList<Integer> friendIdList = addRoomAdapter.getSelectedFriendIdList();
            // TODO
            mAddRoomPresenter.setCreateRoom("5/26 광석 테스트", friendIdList, true);
            dialog.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
        mAlertDialog.show();
    }
}
