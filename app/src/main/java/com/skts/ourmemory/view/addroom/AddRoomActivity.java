package com.skts.ourmemory.view.addroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.presenter.AddRoomPresenter;
import com.skts.ourmemory.view.BaseActivity;

import java.util.Objects;

import butterknife.BindView;

public class AddRoomActivity extends BaseActivity implements AddRoomContract.View {
    private AddRoomPresenter mAddRoomPresenter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_room)
    Toolbar mToolbar;       // Toolbar

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_30);

        // Toolbar 타이틀 없애기
        getSupportActionBar().setTitle("");

        mAddRoomPresenter = new AddRoomPresenter();
        mAddRoomPresenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddRoomPresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
