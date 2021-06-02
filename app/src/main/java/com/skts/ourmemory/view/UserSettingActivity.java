package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.UserSettingContract;
import com.skts.ourmemory.presenter.UserSettingPresenter;

import java.util.Objects;

import butterknife.BindView;

public class UserSettingActivity extends BaseActivity implements UserSettingContract.View {
    private UserSettingPresenter mUserSettingPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_user_setting)
    Toolbar mToolbar;                                   // Toolbar

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        /// Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Toolbar 타이틀 없애기
        getSupportActionBar().setTitle("");

        mUserSettingPresenter = new UserSettingPresenter();
        mUserSettingPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserSettingPresenter.releaseView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
