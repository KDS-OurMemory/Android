package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.presenter.MainPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseActivity;
import com.skts.ourmemory.view.Fragment1;
import com.skts.ourmemory.view.ScheduleActivity;
import com.skts.ourmemory.view.TestActivity;
import com.skts.ourmemory.view.UserSettingActivity;
import com.skts.ourmemory.view.ourmemory.OurMemoryActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mMainPresenter;

    /*Fragment*/
    private FragmentManager mFragmentManager;
    private Fragment mHomeFragment;
    private Fragment mCategoryFragment;
    //private Fragment mMyMemoryFragment;
    private Fragment mOurMemoryFragment;
    private Fragment mMyPageFragment;
    private Fragment mTestFragment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nav_activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_main_bottom_navigation_view_ll)
    LinearLayout mCategoryLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cl_activity_main_bottom_navigation_view_layout)
    ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);

        // 폴링 데이터 처리(방 목록, 일정)
        mMainPresenter.getPollingData();

        mFragmentManager = getSupportFragmentManager();

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
    public Context getAppContext() {
        return this;
    }

    @Override
    public void setInitFragment() {
        mHomeFragment = new HomeFragment();
        mFragmentManager.beginTransaction().replace(R.id.fl_activity_main_frame_layout, mHomeFragment).commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void switchFragment(int id) {
        switch (id) {
            case R.id.item_activity_main_navigation_home:
                // 홈
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mHomeFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().show(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mCategoryFragment).commit();
                }
                if (mTestFragment != null) {
                    mFragmentManager.beginTransaction().hide(mTestFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }

                mCategoryLayout.setVisibility(View.GONE);
                break;
            case R.id.item_activity_main_navigation_category:
                // 카테고리
                if (mCategoryFragment == null) {
                    mCategoryFragment = new CategoryFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mCategoryFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().show(mCategoryFragment).commit();
                }
                if (mTestFragment != null) {
                    mFragmentManager.beginTransaction().hide(mTestFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }

                mCategoryLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.item_activity_main_navigation_my_memory:
                // 나의 기억공간
                //Intent intent = new Intent(this, ScheduleActivity.class);
                /*Intent intent = new Intent(this, TestActivity.class);
                startActivity(intent);*/

                if (mTestFragment == null) {
                    //mTestFragment = new MyMemoryFragment();
                    Display display = getWindowManager().getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int height = point.y;           // 레이아웃 높이

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    float density = displayMetrics.densityDpi / 160f;

                    mTestFragment = new Fragment1(density, height);
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mTestFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mCategoryFragment).commit();
                }
                if (mTestFragment != null) {
                    mFragmentManager.beginTransaction().show(mTestFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_our_memory:
                // 우리의 기억공간
                Intent intent2 = new Intent(this, OurMemoryActivity.class);
                startActivity(intent2);

                if (mOurMemoryFragment == null) {
                    mOurMemoryFragment = new OurMemoryFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mOurMemoryFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mCategoryFragment).commit();
                }
                if (mTestFragment != null) {
                    mFragmentManager.beginTransaction().hide(mTestFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().show(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_my_page:
                // 마이페이지
                Intent intent3 = new Intent(this, UserSettingActivity.class);
                startActivity(intent3);

                if (mMyPageFragment == null) {
                    mMyPageFragment = new MyPageFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mMyPageFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mCategoryFragment).commit();
                }
                if (mTestFragment != null) {
                    mFragmentManager.beginTransaction().hide(mTestFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().show(mMyPageFragment).commit();
                }
                break;
            default:
                DebugLog.e(TAG, "프래그먼트 선택 오류");
                break;
        }
    }
}