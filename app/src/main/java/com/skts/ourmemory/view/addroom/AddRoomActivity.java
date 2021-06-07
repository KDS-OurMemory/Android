package com.skts.ourmemory.view.addroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.skts.ourmemory.common.Const;
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

    private ArrayList<String> mFriendNameList;
    private ArrayList<Integer> mFriendIdList;
    private AddRoomAdapter mAddRoomAdapter;
    private ShowSelectAdapter mShowSelectAdapter;

    /*Dialog*/
    AlertDialog mAlertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mFriendNameList = new ArrayList<>();
        mFriendIdList = new ArrayList<>();
        Intent intent = getIntent();
        mFriendNameList = intent.getStringArrayListExtra(Const.FRIEND_NAME_LIST);
        mFriendIdList = intent.getIntegerArrayListExtra(Const.FRIEND_ID_LIST);

        mAddRoomPresenter = new AddRoomPresenter();
        mAddRoomPresenter.setView(this);

        setInitSetting();
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
    public void setInitSetting() {
        // Set layoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHeaderRecyclerView.setLayoutManager(linearLayoutManager);

        // TODO
        ArrayList<Friend> friendArrayList = new ArrayList<>();
        for (int i = 0; i < mFriendNameList.size(); i++) {
            Friend friend = new Friend(mFriendIdList.get(i), "", mFriendNameList.get(i), false);
            friendArrayList.add(friend);
        }

        mAddRoomAdapter = new AddRoomAdapter(friendArrayList);
        mRecyclerView.setAdapter(mAddRoomAdapter);

        mShowSelectAdapter = new ShowSelectAdapter();
        mHeaderRecyclerView.setAdapter(mShowSelectAdapter);

        mAddRoomAdapter.setOnItemClickListener((view, position) -> DebugLog.e("testtt", "" + position));

        mAddRoomAdapter.setOnClickListener((view, position) -> {
            if (mAddRoomAdapter.getItem(position).isSelectStatus()) {
                mAddRoomAdapter.getItem(position).setSelectStatus(false);
                // TODO
                mShowSelectAdapter.deleteListItem(position);
            } else {
                mAddRoomAdapter.getItem(position).setSelectStatus(true);
                // TODO
                SelectPerson selectPerson = new SelectPerson(mFriendIdList.get(position), "", mFriendNameList.get(position), position);
                mShowSelectAdapter.addItem(selectPerson);
            }
            checkCount(mAddRoomAdapter.getSelectCount());
        });

        mShowSelectAdapter.setOnItemClickListener((view, position) -> {
            int index = mShowSelectAdapter.getItem(position).getIndex();
            mShowSelectAdapter.deleteShowTopListItem(position);
            mAddRoomAdapter.getItem(index).setSelectStatus(!mAddRoomAdapter.getItem(index).isSelectStatus());

            checkCount(mAddRoomAdapter.getSelectCount());
        });

        // 버튼 비활성화
        mCreateButton.setEnabled(false);
        mShowSelectNumber.setEnabled(false);
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
        mAddRoomAdapter.setNotifyDataSetChanged();
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
            ArrayList<Integer> friendIdList = mAddRoomAdapter.getSelectedFriendIdList();
            // TODO
            mAddRoomPresenter.setCreateRoom("5/26 광석 테스트", friendIdList, true);
            dialog.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        mAlertDialog.show();
    }
}
