package com.skts.ourmemory.view.main;

import android.view.View;
import android.widget.LinearLayout;

import com.skts.ourmemory.R;
import com.skts.ourmemory.view.BaseFragment;

public class CategoryFragment extends BaseFragment {
    private final LinearLayout mLinearLayout;

    public CategoryFragment(LinearLayout linearLayout) {
        linearLayout.setVisibility(View.VISIBLE);
        mLinearLayout = linearLayout;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_category;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            mLinearLayout.setVisibility(View.GONE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
        }
        super.onHiddenChanged(hidden);
    }
}
