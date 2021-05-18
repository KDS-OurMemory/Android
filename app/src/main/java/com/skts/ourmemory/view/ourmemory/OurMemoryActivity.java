package com.skts.ourmemory.view.ourmemory;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.OurMemoryViewPageAdapter;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.presenter.OurMemoryPresenter;
import com.skts.ourmemory.view.BaseActivity;

import butterknife.BindView;

public class OurMemoryActivity extends BaseActivity implements OurMemoryContract.View {
    private OurMemoryPresenter mOurMemoryPresenter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_activity_our_memory_view_pager)
    ViewPager mViewPager;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_activity_out_memory_tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_memory);

        mOurMemoryPresenter = new OurMemoryPresenter();
        mOurMemoryPresenter.setView(this);

        mTabLayout.addTab(mTabLayout.newTab().setText("친구 목록"));
        mTabLayout.addTab(mTabLayout.newTab().setText("방 목록"));

        mViewPager.setAdapter(new OurMemoryViewPageAdapter(getSupportFragmentManager()));

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
}
