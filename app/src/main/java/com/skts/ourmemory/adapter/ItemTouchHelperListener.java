package com.skts.ourmemory.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onLeftClick(int position, RecyclerView.ViewHolder viewHolder);
    void onRightClick(int position, RecyclerView.ViewHolder viewHolder);
    void onItemSelectedChange(RecyclerView.ViewHolder viewHolder);
    void onItemClear(RecyclerView.ViewHolder viewHolder);
}
