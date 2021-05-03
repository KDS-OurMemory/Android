package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.presenter.MainPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.ScheduleActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mMainPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nav_activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);

        // 기본 프래그먼트 지정(홈)
        setInitFragment();

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switchFragment(item.getItemId());
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.releaseView();
    }

    @Override
    public void setInitFragment() {
        Fragment fragment = HomeFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main_frame_layout, fragment)
                .commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void switchFragment(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.item_activity_main_navigation_home:
                // 홈
                fragment = HomeFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_category:
                // 카테고리
                fragment = CategoryFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_my_memory:
                // 나의 기억공간
                Intent intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);

                /*fragment = MyMemoryFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();*/
                break;
            case R.id.item_activity_main_navigation_our_memory:
                // 우리의 기억공간
                fragment = OurMemoryFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_my_page:
                // 마이페이지
                fragment = MyPageFragment.newInstance();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            default:
                DebugLog.e(TAG, "프래그먼트 선택 오류");
                break;
        }
    }
}