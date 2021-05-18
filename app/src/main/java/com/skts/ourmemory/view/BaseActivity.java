package com.skts.ourmemory.view;

import android.os.SystemClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private long mLastClickTime = 0;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isDuplicate() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime > 500) {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }
}
