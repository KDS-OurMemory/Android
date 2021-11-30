package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.ShareViewPagerAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.ShareContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.presenter.SharePresenter;
import com.skts.ourmemory.view.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity implements ShareContract.View {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_activity_share_view_pager)
    ViewPager mViewPager;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_activity_share_tab_layout)
    TabLayout mTabLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_activity_share_check_button)
    TextView mCheckBtn;

    private ShareContract.Presenter mPresenter;
    private ShareViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initSet();
    }

    @Override
    public void initSet() {
        mPresenter = new SharePresenter();
        mPresenter.setView(this);

        mAdapter = new ShareViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);

        // 연동
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void showFriendData(FriendPostResult friendPostResult) {
        SeparateShareFragment separateShareFragment = (SeparateShareFragment) mAdapter.getItem(0);      // SeparateShareFragment
        if (separateShareFragment.isVisible()) {
            // 따로 따로
            separateShareFragment.showFriendData(friendPostResult);
        }

        TogetherShareFragment togetherShareFragment = (TogetherShareFragment) mAdapter.getItem(1);      // TogetherFragment
        if (togetherShareFragment.isVisible()) {
            // 묶어서
            togetherShareFragment.showFriendData(friendPostResult);
        }
    }

    @Override
    public void showRoomData(RoomPostResult roomPostResult) {
        RoomShareFragment roomShareFragment = (RoomShareFragment) mAdapter.getItem(2);                  // RoomShareFragment
        if (roomShareFragment.isVisible()) {
            // 기존 방
            roomShareFragment.showRoomData(roomPostResult);
        }
    }

    @Override
    public FriendPostResult getFriendData() {
        return mPresenter.getFriendPostResult();
    }

    @Override
    public RoomPostResult getRoomData() {
        return mPresenter.getRoomPostResult();
    }

    @Override
    public int getPrivateRoomId() {
        return mPresenter.getPrivateRoomId();
    }

    public void changeCheckBtn(int count) {
        if (count == 0) {
            mCheckBtn.setEnabled(false);
            return;
        }
        mCheckBtn.setEnabled(true);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_share_close_button)
    void onClickCloseBtn() {
        onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_share_check_button)
    void onClickCheckBtn() {
        switch (mTabLayout.getSelectedTabPosition()) {
            case 0:
                // 따로 따로
                SeparateShareFragment separateShareFragment = (SeparateShareFragment) mAdapter.getItem(0);      // SeparateShareFragment
                List<Object> list = separateShareFragment.getShareList();
                List<Integer> sepIntList = new ArrayList<>();
                List<String> sepStringList = new ArrayList<>();

                for (Object data : list) {
                    if (data instanceof Integer) {
                        sepIntList.add((Integer) data);
                    } else {
                        // String
                        sepStringList.add((String) data);
                    }
                }

                Intent intent = new Intent();
                intent.putExtra(Const.SHARE_DATA_ID, (Serializable) sepIntList);
                intent.putExtra(Const.SHARE_DATA_NAME, (Serializable) sepStringList);
                intent.putExtra(Const.SHARE_DATA_TYPE, ServerConst.SHARED_TYPE_USERS);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case 1:
                // 묶어서
                TogetherShareFragment togetherShareFragment = (TogetherShareFragment) mAdapter.getItem(1);      // TogetherFragment
                List<Object> list2 = togetherShareFragment.getShareList();
                List<Integer> togeIntList = new ArrayList<>();
                List<String> togeStringList = new ArrayList<>();

                for (Object data : list2) {
                    if (data instanceof Integer) {
                        togeIntList.add((Integer) data);
                    } else {
                        // String
                        togeStringList.add((String) data);
                    }
                }

                Intent intent2 = new Intent();
                intent2.putExtra(Const.SHARE_DATA_ID, (Serializable) togeIntList);
                intent2.putExtra(Const.SHARE_DATA_NAME, (Serializable) togeStringList);
                intent2.putExtra(Const.SHARE_DATA_TYPE, ServerConst.SHARED_TYPE_USER_GROUP);
                setResult(RESULT_OK, intent2);
                finish();
                break;
            case 2:
                // 기존 방
                RoomShareFragment roomShareFragment = (RoomShareFragment) mAdapter.getItem(2);                  // RoomShareFragment
                List<Object> list3 = roomShareFragment.getShareList();
                List<Integer> roomIntList = new ArrayList<>();
                List<String> roomStringList = new ArrayList<>();

                roomIntList.add((Integer) list3.get(0));
                roomStringList.add((String) list3.get(1));

                Intent intent3 = new Intent();
                intent3.putExtra(Const.SHARE_DATA_ID, (Serializable) roomIntList);
                intent3.putExtra(Const.SHARE_DATA_NAME, (Serializable) roomStringList);
                intent3.putExtra(Const.SHARE_DATA_TYPE, ServerConst.SHARED_TYPE_ROOMS);
                setResult(RESULT_OK, intent3);
                finish();
                break;
        }
    }
}
