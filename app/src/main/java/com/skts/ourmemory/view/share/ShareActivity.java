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
import com.skts.ourmemory.presenter.SharePresenter;
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

        ShareViewPagerAdapter adapter = new ShareViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);

        // 연동
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public Context getAppContext() {
        return this;
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
