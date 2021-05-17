package com.skts.ourmemory.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;

public class OurMemoryViewHolderPage extends RecyclerView.ViewHolder {
    private TextView title;
    private RelativeLayout relativeLayout;

    DataPage data;

    OurMemoryViewHolderPage(View view) {
        super(view);
        title = view.findViewById(R.id.tv_title);
        relativeLayout = view.findViewById(R.id.rl_layout);
    }

    public void onBind(DataPage data) {
        this.data = data;

        title.setText(data.getTitle());
        relativeLayout.setBackgroundResource(data.getColor());
    }
}
