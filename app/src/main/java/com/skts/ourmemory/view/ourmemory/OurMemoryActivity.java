package com.skts.ourmemory.view.ourmemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.OurMemoryViewPageAdapter;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.presenter.OurMemoryPresenter;
import com.skts.ourmemory.view.BaseActivity;
import com.skts.ourmemory.view.addfriend.AddFriendActivity;

import java.util.List;

import butterknife.BindView;

public class OurMemoryActivity extends BaseActivity implements OurMemoryContract.View {
    private OurMemoryPresenter mOurMemoryPresenter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_activity_our_memory_view_pager)
    ViewPager mViewPager;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_activity_out_memory_tab_layout)
    TabLayout mTabLayout;

    OurMemoryViewPageAdapter mOurMemoryViewPageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_memory);

        mOurMemoryPresenter = new OurMemoryPresenter();
        mOurMemoryPresenter.setView(this);

        // 친구목록 조회
        mOurMemoryPresenter.getFriendList();

        mTabLayout.addTab(mTabLayout.newTab().setText("친구 목록"));
        mTabLayout.addTab(mTabLayout.newTab().setText("방 목록"));

        mOurMemoryViewPageAdapter = new OurMemoryViewPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mOurMemoryViewPageAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
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
    protected void onDestroy() {
        super.onDestroy();
        mOurMemoryPresenter.releaseView();
    }

    @Override
    public void startAddFriendActivity() {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void showFriendList(List<FriendPostResult.ResponseValue> responseValueList) {
        FriendListFragment friendListFragment = (FriendListFragment) mOurMemoryViewPageAdapter.getItem(0);
        friendListFragment.showFriendList(responseValueList);
    }
}
