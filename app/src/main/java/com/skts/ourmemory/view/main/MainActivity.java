package com.skts.ourmemory.view.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

public class MainActivity extends BaseActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mMainPresenter;

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
        Fragment fragment = new HomeFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_activity_main_frame_layout, fragment)
                .commit();
    }

    @Override
    public void switchFragment(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.item_activity_main_navigation_home:
                // 홈
                fragment = new HomeFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_category:
                // 카테고리
                fragment = new CategoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_my_memory:
                // 나의 기억공간
                fragment = new MyMemoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_our_memory:
                // 우리의 기억공간
                fragment = new OurMemoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_activity_main_frame_layout, fragment)
                        .commit();
                break;
            case R.id.item_activity_main_navigation_my_page:
                // 마이페이지
                fragment = new MyPageFragment();
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