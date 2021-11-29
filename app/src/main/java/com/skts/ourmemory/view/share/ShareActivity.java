package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.ShareViewPagerAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.ShareContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.presenter.SharePresenter;
import com.skts.ourmemory.view.BaseActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ShareContract.View {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_activity_share_view_pager)
    ViewPager mViewPager;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_activity_share_tab_layout)
    TabLayout mTabLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_activity_share_check_button)
    TextView mCheckBtn;

    private ShareContract.Presenter mPresenter;
    private ShareViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initSet();
    }

    @Override
    public void initSet() {
        mPresenter = new SharePresenter();
        mPresenter.setView(this);

        mAdapter = new ShareViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);

        // 연동
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void showFriendData(FriendPostResult friendPostResult) {
        SeparateShareFragment separateShareFragment = (SeparateShareFragment) mAdapter.getItem(0);      // SeparateShareFragment
        if (separateShareFragment.isVisible()) {
            // 따로 따로
            separateShareFragment.showFriendData(friendPostResult);
        }

        TogetherShareFragment togetherShareFragment = (TogetherShareFragment) mAdapter.getItem(1);      // TogetherFragment
        if (togetherShareFragment.isVisible()) {
            // 묶어서
            togetherShareFragment.showFriendData(friendPostResult);
        }
    }

    @Override
    public void showRoomData(RoomPostResult roomPostResult) {
        RoomShareFragment roomShareFragment = (RoomShareFragment) mAdapter.getItem(2);                  // RoomShareFragment
        if (roomShareFragment.isVisible()) {
            // 기존 방
            roomShareFragment.showRoomData(roomPostResult);
        }
    }

    @Override
    public FriendPostResult getFriendData() {
        return mPresenter.getFriendPostResult();
    }

    @Override
    public RoomPostResult getRoomData() {
        return mPresenter.getRoomPostResult();
    }

    @Override
    public int getPrivateRoomId() {
        return mPresenter.getPrivateRoomId();
    }

    public void changeCheckBtn(int count) {
        if (count == 0) {
            mCheckBtn.setEnabled(false);
            return;
        }
        mCheckBtn.setEnabled(true);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_share_close_button)
    void onClickCloseBtn() {
        onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_share_check_button)
    void onClickCheckBtn() {
        switch (mTabLayout.getSelectedTabPosition()) {
            case 0:
                // 따로 따로
                SeparateShareFragment separateShareFragment = (SeparateShareFragment) mAdapter.getItem(0);      // SeparateShareFragment
                List<Integer> list = separateShareFragment.getShareList();

                Intent intent = new Intent();
                intent.putExtra(Const.SHARE_DATA, (Serializable) list);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 1:
                // 묶어서
                TogetherShareFragment togetherShareFragment = (TogetherShareFragment) mAdapter.getItem(1);      // TogetherFragment
                List<Integer> list2 = togetherShareFragment.getShareList();

                Intent intent2 = new Intent();
                intent2.putExtra(Const.SHARE_DATA, (Serializable) list2);
                setResult(RESULT_OK, intent2);
                finish();
                break;
            case 2:
                // 기존 방
                showToast("기존 방");
                break;
        }
    }
}
