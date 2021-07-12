package com.skts.ourmemory.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.skts.ourmemory.view.ourmemory.FriendListFragment;
import com.skts.ourmemory.view.ourmemory.OurRoomFragment;

import java.util.ArrayList;
import java.util.List;

public class OurMemoryViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList;

    public OurMemoryViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragmentList = new ArrayList<>();
        fragmentList.add(new FriendListFragment());
        fragmentList.add(new OurRoomFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
