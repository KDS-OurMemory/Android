package com.skts.ourmemory.view.addfriend;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.AddFriendContract;
import com.skts.ourmemory.presenter.AddFriendPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseActivity;

import java.util.Objects;

import butterknife.BindView;

public class AddFriendActivity extends BaseActivity implements AddFriendContract.View {
    private final String TAG = AddFriendActivity.class.getSimpleName();

    private AddFriendPresenter mAddFriendPresenter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_friend)
    Toolbar mToolbar;       // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_activity_add_friend_tab_layout)
    TabLayout mTabLayout;

    /*Fragment*/
    private FragmentManager mFragmentManager;
    private Fragment mRecommendFragment;
    private Fragment mQRFragment;
    private Fragment mIdFragment;
    private Fragment mNameFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_30);

        // Toolbar 타이틀 없애기
        getSupportActionBar().setTitle("");

        mAddFriendPresenter = new AddFriendPresenter();
        mAddFriendPresenter.setView(this);

        mFragmentManager = getSupportFragmentManager();

        // 기본 프래그먼트 지정(추천)
        setInitFragment();

        mTabLayout.addTab(mTabLayout.newTab().setText("추천"));
        mTabLayout.addTab(mTabLayout.newTab().setText("QR"));
        mTabLayout.addTab(mTabLayout.newTab().setText("ID"));
        mTabLayout.addTab(mTabLayout.newTab().setText("이름"));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment(tab.getPosition());
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
        mAddFriendPresenter.releaseView();
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
    public void setInitFragment() {
        mRecommendFragment = new RecommendFragment();
        mFragmentManager.beginTransaction().replace(R.id.fl_activity_add_friend_frame_layout, mRecommendFragment).commit();
    }

    @Override
    public void switchFragment(int index) {
        switch (index) {
            case 0:
                // 추천
                if (mRecommendFragment == null) {
                    mRecommendFragment = new RecommendFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_add_friend_frame_layout, mRecommendFragment).commit();
                }

                if (mRecommendFragment != null) {
                    mFragmentManager.beginTransaction().show(mRecommendFragment).commit();
                }
                if (mQRFragment != null) {
                    mFragmentManager.beginTransaction().hide(mQRFragment).commit();
                }
                if (mIdFragment != null) {
                    mFragmentManager.beginTransaction().hide(mIdFragment).commit();
                }
                if (mNameFragment != null) {
                    mFragmentManager.beginTransaction().hide(mNameFragment).commit();
                }
                break;
            case 1:
                // QR
                if (mQRFragment == null) {
                    mQRFragment = new QRFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_add_friend_frame_layout, mQRFragment).commit();
                }

                if (mRecommendFragment != null) {
                    mFragmentManager.beginTransaction().hide(mRecommendFragment).commit();
                }
                if (mQRFragment != null) {
                    mFragmentManager.beginTransaction().show(mQRFragment).commit();
                }
                if (mIdFragment != null) {
                    mFragmentManager.beginTransaction().hide(mIdFragment).commit();
                }
                if (mNameFragment != null) {
                    mFragmentManager.beginTransaction().hide(mNameFragment).commit();
                }
                break;
            case 2:
                // ID
                if (mIdFragment == null) {
                    mIdFragment = new IdFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_add_friend_frame_layout, mIdFragment).commit();
                }

                if (mRecommendFragment != null) {
                    mFragmentManager.beginTransaction().hide(mRecommendFragment).commit();
                }
                if (mQRFragment != null) {
                    mFragmentManager.beginTransaction().hide(mQRFragment).commit();
                }
                if (mIdFragment != null) {
                    mFragmentManager.beginTransaction().show(mIdFragment).commit();
                }
                if (mNameFragment != null) {
                    mFragmentManager.beginTransaction().hide(mNameFragment).commit();
                }
                break;
            case 3:
                // 이름
                if (mNameFragment == null) {
                    mNameFragment = new NameFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_add_friend_frame_layout, mNameFragment).commit();
                }

                if (mRecommendFragment != null) {
                    mFragmentManager.beginTransaction().hide(mRecommendFragment).commit();
                }
                if (mQRFragment != null) {
                    mFragmentManager.beginTransaction().hide(mQRFragment).commit();
                }
                if (mIdFragment != null) {
                    mFragmentManager.beginTransaction().hide(mIdFragment).commit();
                }
                if (mNameFragment != null) {
                    mFragmentManager.beginTransaction().show(mNameFragment).commit();
                }
                break;
            default:
                DebugLog.e(TAG, "프래그먼트 선택 오류");
                break;
        }
    }
}
