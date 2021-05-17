package com.skts.ourmemory.view.ourmemory;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.DataPage;
import com.skts.ourmemory.adapter.OurMemoryViewPageAdapter;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.presenter.OurMemoryPresenter;

import java.util.ArrayList;

import butterknife.BindView;

public class OurMemoryActivity extends BaseActivity implements OurMemoryContract.View {
    private final String TAG = OurMemoryActivity.class.getSimpleName();

    private OurMemoryPresenter mOurMemoryPresenter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_activity_our_memory_view_pager)
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_memory);

        mOurMemoryPresenter = new OurMemoryPresenter();
        mOurMemoryPresenter.setView(this);

        ArrayList<DataPage> dataPages = new ArrayList<>();
        dataPages.add(new DataPage(android.R.color.black, "1 Page"));
        dataPages.add(new DataPage(android.R.color.holo_red_light, "2 Page"));
        dataPages.add(new DataPage(android.R.color.holo_green_light, "3 Page"));
        dataPages.add(new DataPage(android.R.color.holo_orange_light, "4 Page"));
        dataPages.add(new DataPage(android.R.color.holo_blue_light, "5 Page"));

        viewPager2.setAdapter(new OurMemoryViewPageAdapter(dataPages));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOurMemoryPresenter.releaseView();
    }
}
