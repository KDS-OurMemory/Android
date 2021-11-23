package com.skts.ourmemory.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.AddRoomAdapter;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.model.friend.Friend;
import com.skts.ourmemory.model.friend.FriendDAO;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SeparateShareFragment extends BaseFragment {
    private Unbinder unbinder;
    private Context mContext;
    private AddRoomAdapter mAdapter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_share_recyclerview)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_fragment_share_separate_search)
    EditText mSearchText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_share_separate_no_friend_text)
    TextView mNoFriendText;

    public SeparateShareFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_separate, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = Objects.requireNonNull(container).getContext();

        initSet();
        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share_separate;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void initSet() {
        setRecycler();
        showFriendData(((ShareActivity) Objects.requireNonNull(getActivity())).getFriendData());
    }

    public void setRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void showFriendData(FriendPostResult friendPostResult) {
        if (friendPostResult == null) {
            return;
        }

        List<FriendDAO> friendData = friendPostResult.getResponse();
        if (friendData.isEmpty()) {
            // 친구 목록 없음
            showNoFriend(true);
            return;
        }
        showNoFriend(false);

        ArrayList<Friend> friendList = new ArrayList<>();
        for (int i = 0; i < friendData.size(); i++) {
            FriendDAO responseValue = friendData.get(i);
            String status = responseValue.getFriendStatus();
            if (status.equals(ServerConst.FRIEND)) {
                // 친구
                Friend friend = new Friend(
                        responseValue.getFriendId(),
                        responseValue.getProfileImageUrl(),
                        responseValue.getName(),
                        responseValue.getBirthday(),
                        responseValue.isSolar(),
                        responseValue.isBirthdayOpen(),
                        false
                );
                friendList.add(friend);
            }
        }

        mAdapter = new AddRoomAdapter(friendList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, position) -> {
            DebugLog.e("testtt", "hhhh");
        });

        mAdapter.setOnClickListener((view, position) -> {
            if (mAdapter.getItem(position).isSelectStatus()) {
                mAdapter.getItem(position).setSelectStatus(false);
                mAdapter.setCheckCount(-1);
            } else {
                mAdapter.getItem(position).setSelectStatus(true);
                mAdapter.setCheckCount(+1);
            }
            ((ShareActivity) Objects.requireNonNull(getActivity())).changeCheckBtn(mAdapter.getCheckCount());
            mAdapter.setNotifyDataSetChanged();
        });
    }

    public void showNoFriend(boolean status) {
        if (status) {
            mNoFriendText.setVisibility(View.VISIBLE);
        } else {
            mNoFriendText.setVisibility(View.GONE);
        }
    }
}
