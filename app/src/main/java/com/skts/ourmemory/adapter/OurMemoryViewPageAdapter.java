package com.skts.ourmemory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;

import java.util.ArrayList;

public class OurMemoryViewPageAdapter extends RecyclerView.Adapter<OurMemoryViewHolderPage> {
    private ArrayList<DataPage> mDataPageArrayList;

    public OurMemoryViewPageAdapter(ArrayList<DataPage> dataPageArrayList) {
        this.mDataPageArrayList = dataPageArrayList;
    }

    @NonNull
    @Override
    public OurMemoryViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_our_memory_viewpager, parent, false);
        return new OurMemoryViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OurMemoryViewHolderPage holder, int position) {
        ((OurMemoryViewHolderPage) holder).onBind(mDataPageArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataPageArrayList.size();
    }
}
