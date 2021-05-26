package com.skts.ourmemory.view.main;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.presenter.MainPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseActivity;
import com.skts.ourmemory.view.ScheduleActivity;
import com.skts.ourmemory.view.ourmemory.OurMemoryActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mMainPresenter;

    /*Fragment*/
    private FragmentManager mFragmentManager;
    private Fragment mHomeFragment;
    private Fragment mCategoryFragment;
    private Fragment mMyMemoryFragment;
    private Fragment mOurMemoryFragment;
    private Fragment mMyPageFragment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nav_activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_main_bottom_navigation_view_ll)
    LinearLayout mCategoryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);

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
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_category:
                // 카테고리
                if (mCategoryFragment == null) {
                    //mCategoryFragment = new CategoryFragment();
                    //mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mCategoryFragment).commit();
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 800, 0, 1000);
                    translateAnimation.setDuration(1000);
                    mCategoryLayout.startAnimation(translateAnimation);
                    mCategoryLayout.setVisibility(View.VISIBLE);
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().show(mCategoryFragment).commit();
                }
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_my_memory:
                // 나의 기억공간
                Intent intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);

                if (mMyMemoryFragment == null) {
                    mMyMemoryFragment = new MyMemoryFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mMyMemoryFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mCategoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mCategoryFragment).commit();
                }
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().show(mMyMemoryFragment).commit();
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
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
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
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
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