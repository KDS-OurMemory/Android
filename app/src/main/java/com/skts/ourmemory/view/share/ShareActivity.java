package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.ShareViewPagerAdapter;
import com.skts.ourmemory.contract.ShareContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.presenter.SharePresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ShareContract.View {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_activity_share_view_pager)
    ViewPager mViewPager;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_activity_share_tab_layout)
    TabLayout mTabLayout;

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

        // 연동
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    SeparateShareFragment separateShareFragment = (SeparateShareFragment) mAdapter.getItem(0);      // SeparateShareFragment
                    if (separateShareFragment != null) {
                        separateShareFragment.showFriendData();
                    }
                } else if (tab.getPosition() == 1) {
                    TogetherShareFragment togetherShareFragment = (TogetherShareFragment) mAdapter.getItem(1);      // TogetherFragment
                    if (togetherShareFragment != null) {
                        togetherShareFragment.showFriendData();
                    }
                } else {
                    RoomShareFragment roomShareFragment = (RoomShareFragment) mAdapter.getItem(2);                  // RoomShareFragment
                    if (roomShareFragment != null) {
                        DebugLog.e("testtt", "222222");
                        roomShareFragment.showRoomData();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void showFriendData(FriendPostResult friendPostResult) {
        SeparateShareFragment separateShareFragment = (SeparateShareFragment) mAdapter.getItem(0);      // SeparateShareFragment
        if (separateShareFragment != null) {
            separateShareFragment.setFriendData(friendPostResult);
            if (separateShareFragment.isVisible()) {
                // 따로 따로
                separateShareFragment.showFriendData();
            }
        }

        TogetherShareFragment togetherShareFragment = (TogetherShareFragment) mAdapter.getItem(1);      // TogetherFragment
        if (togetherShareFragment != null) {
            togetherShareFragment.setFriendData(friendPostResult);
            if (togetherShareFragment.isVisible()) {
                // 묶어서
                togetherShareFragment.showFriendData();
            }
        }
    }

    @Override
    public void showRoomData(RoomPostResult roomPostResult) {
        RoomShareFragment roomShareFragment = (RoomShareFragment) mAdapter.getItem(2);                  // RoomShareFragment
        if (roomShareFragment != null) {
            roomShareFragment.setRoomData(roomPostResult);
            if (roomShareFragment.isVisible()) {
                // 기존 방
                roomShareFragment.showRoomData();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_share_close_button)
    void onClickCloseBtn() {
        onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_share_check_button)
    void onClickCheckBtn() {
        showToast("다음");
    }
}
