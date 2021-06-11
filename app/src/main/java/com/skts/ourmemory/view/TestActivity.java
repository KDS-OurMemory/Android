package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.GridAdapter;
import com.skts.ourmemory.adapter.SimpleTextAdapter;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    TextView textView;
    LinearLayout linearLayout;
    LinearLayout allLinearLayout;
    LinearLayout testLayout;
    float firstY;
    float mHeight;
    float distance;
    private GridAdapter mGridAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //textView = findViewById(R.id.activity_test_text);
        linearLayout = findViewById(R.id.activity_test_ll);
        testLayout = findViewById(R.id.recycler_ll);
        allLinearLayout = findViewById(R.id.activity_test_all_ll);
        allLinearLayout.setClickable(true);

        mGridAdapter = new GridAdapter(getApplicationContext(), getDayList());

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.format("TEXT %d", i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SimpleTextAdapter adapter = new SimpleTextAdapter(list);
        recyclerView.setAdapter(adapter);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = layoutInflater.inflate(R.layout.activity_test_item, null, false);

        allLinearLayout.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    firstY = motionEvent.getY();
                    mHeight = linearLayout.getHeight();
                    break;

                case MotionEvent.ACTION_MOVE:
                    distance = firstY - motionEvent.getY();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayout.getWidth(), (int) (mHeight + distance));
                    linearLayout.setLayoutParams(params);

                    /*LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(testLayout.getWidth(), (int) (mHeight - distance));
                    testLayout.setLayoutParams(params2);*/

                    adapter.setHeight((int) (mHeight - distance) / 10);
                    break;
            }
            return false;
        });

    }

    private List<String> getDayList() {
        List<String> mDayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDayList.add("" + (i + 1));
        }

        return mDayList;
    }
}
