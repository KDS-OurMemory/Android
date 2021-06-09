package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skts.ourmemory.R;

public class TestActivity extends AppCompatActivity {
    TextView textView;
    LinearLayout linearLayout;
    LinearLayout allLinearLayout;
    float firstY;
    float mHeight;
    float distance;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = findViewById(R.id.activity_test_text);
        linearLayout = findViewById(R.id.activity_test_ll);
        allLinearLayout = findViewById(R.id.activity_test_all_ll);
        allLinearLayout.setClickable(true);

        allLinearLayout.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstY = motionEvent.getY();
                    mHeight = textView.getHeight();
                    break;

                case MotionEvent.ACTION_MOVE:
                    distance = motionEvent.getY() - firstY;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(textView.getWidth(), (int) (mHeight - distance));
                    linearLayout.setLayoutParams(params);
                    break;
            }
            return false;
        });


    }
}
