package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;

import java.util.Objects;

import butterknife.BindView;

public class EditMyPageActivity extends BaseActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_edit_my_page)
    Toolbar mToolbar;       // Toolbar

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_page);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
    }
}
