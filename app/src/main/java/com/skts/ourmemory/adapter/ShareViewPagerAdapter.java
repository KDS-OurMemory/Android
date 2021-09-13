package com.skts.ourmemory.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.skts.ourmemory.view.share.RoomShareFragment;
import com.skts.ourmemory.view.share.SeparateShareFragment;
import com.skts.ourmemory.view.share.TogetherShareFragment;

import java.util.ArrayList;

public class ShareViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> items;
    private final ArrayList<String> texts;

    public ShareViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        items = new ArrayList<>();
        texts = new ArrayList<>();

        items.add(new SeparateShareFragment());
        items.add(new TogetherShareFragment());
        items.add(new RoomShareFragment());

        texts.add("따로 따로");
        texts.add("묶어서");
        texts.add("기존 방");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return texts.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
