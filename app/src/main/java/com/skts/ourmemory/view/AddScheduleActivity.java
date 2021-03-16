package com.skts.ourmemory.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.AddScheduleContract;

public class AddScheduleActivity extends BaseActivity implements AddScheduleContract.View {
    private final String TAG = AddScheduleActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
    }
}
