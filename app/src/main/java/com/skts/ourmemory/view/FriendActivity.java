package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.contract.FriendContract;
import com.skts.ourmemory.model.user.UserDAO;
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
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_friend_request_friend_number)
    TextView mRequestFriendNumber;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_activity_friend_request_friend_arrow)
    ImageView mArrowImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_friend_request_friend_visible)
    LinearLayout mRequestFriendLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_friend_have_friends)
    LinearLayout mFriendLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_friend_have_friends_number)
    TextView mHaveFriendsNumber;

    /*Dialog*/
    AlertDialog mAlertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

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

        mPresenter = new FriendPresenter();
        mPresenter.setView(this);

        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        // 친구 데이터 요청
        mRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRequestRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        // 친구 목록
        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFriendRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
    }

    @Override
    public void initAdapter(RequestFriendListAdapter requestFriendListAdapter, FriendListAdapter friendListAdapter) {
        mRequestRecyclerView.setAdapter(requestFriendListAdapter);
        mFriendRecyclerView.setAdapter(friendListAdapter);
    }

    @Override
    public void setRequestAdapter(RequestFriendListAdapter requestAdapter, FriendListAdapter friendListAdapter) {
        mRequestRecyclerView.setAdapter(requestAdapter);
        mFriendRecyclerView.setAdapter(friendListAdapter);

        requestAdapter.setOnItemClickListener((view, position) -> {
            UserDAO userDAO = requestAdapter.getData().get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mAlertDialog = builder.create();
            mAlertDialog.setTitle("친구 추가");
            mAlertDialog.setMessage(userDAO.getName() + "님을 친구추가 하시겠습니까?");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
                // 친구 요청 수락
                mPresenter.requestAcceptFriend(userDAO);
                dialog.dismiss();
            });
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
            mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
            mAlertDialog.show();
        });
    }

    @Override
    public void showNoFriend(boolean status) {
        if (status) {
            mNoFriendText.setVisibility(View.VISIBLE);
            mRequestFriendLayout.setVisibility(View.GONE);
            mFriendLayout.setVisibility(View.GONE);
        } else {
            mNoFriendText.setVisibility(View.GONE);
            mRequestFriendLayout.setVisibility(View.VISIBLE);
            mFriendLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showRequestFriendNumber(int count) {
        mRequestFriendNumber.setText(String.valueOf(count));
    }

    @Override
    public void hideRequestFriend() {
        mRequestFriendLayout.setVisibility(View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_friend_list_search_friend)
    void onClickSearchFriendBtn() {
        if (mSearchText.getVisibility() == View.VISIBLE) {
            mSearchText.setVisibility(View.GONE);
        } else {
            mSearchText.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_friend_list_add_friend)
    void onClickAddFriendBtn() {
        startActivity(new Intent(this, AddFriendActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_friend_list_setting)
    void onClickSettingBtn() {
        showToast("친구 목록 설정 버튼");
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_activity_friend_request_friend_visible)
    void onClickRequestFriendVisible() {
        if (mPresenter.isRequestArrowCollapsible()) {
            // 접혀져 있으면
            // 펴기
            mArrowImage.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_30);
            mPresenter.setRequestArrowCollapsible(false);
            RequestFriendListAdapter adapter = mPresenter.getAdapter();
            adapter.addAllItem();
        } else {
            // 펴져 있으면
            // 접기
            mPresenter.setRequestArrowCollapsible(true);
            mArrowImage.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_30);
            RequestFriendListAdapter adapter = mPresenter.getAdapter();
            adapter.removeAllItem();
        }
    }
}
