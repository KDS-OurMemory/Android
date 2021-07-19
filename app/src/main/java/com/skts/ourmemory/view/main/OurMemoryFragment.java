package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.OurMemoryViewPagerAdapter;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.presenter.OurMemoryPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OurMemoryFragment extends BaseFragment implements OurMemoryContract.View {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_fragment_main_our_memory_tab_layout)
    TabLayout mTabLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_fragment_main_our_memory_view_pager)
    ViewPager mViewPager;

    private Unbinder unbinder;
    private OurMemoryContract.Presenter mPresenter;
    private OurMemoryViewPagerAdapter mOurMemoryViewPagerAdapter;


    public OurMemoryFragment() {
        mPresenter = new OurMemoryPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_our_memory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_our_memory, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mPresenter.setView(this);

        initView(rootView);
        initSet();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.releaseView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public Context getAppContext() {
        return Objects.requireNonNull(getActivity()).getApplicationContext();
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void initSet() {
        mTabLayout.addTab(mTabLayout.newTab().setText("친구 목록"));
        mTabLayout.addTab(mTabLayout.newTab().setText("방 목록"));

        mOurMemoryViewPagerAdapter = new OurMemoryViewPagerAdapter(((MainActivity) Objects.requireNonNull(getActivity())).getMyFragmentManager());
        mViewPager.setAdapter(mOurMemoryViewPagerAdapter);

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
}
