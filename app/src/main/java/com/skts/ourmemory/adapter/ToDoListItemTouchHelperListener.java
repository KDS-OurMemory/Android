package com.skts.ourmemory.adapter;

import androidx.recyclerview.widget.RecyclerView;

public interface ToDoListItemTouchHelperListener {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemSelectedChange(RecyclerView.ViewHolder viewHolder);
    void onItemClear(RecyclerView.ViewHolder viewHolder);
}
