package com.skts.ourmemory.view.ourmemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.contract.FriendListContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.presenter.FriendListPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendListFragment extends BaseFragment implements FriendListContract.View {
    private FriendListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mFriendNameList;
    private ArrayList<Integer> mFriendIdList;

    public FriendListFragment() {
    }

    public ArrayList<String> getFriendNameList() {
        return mFriendNameList;
    }

    public ArrayList<Integer> getFriendIdList() {
        return mFriendIdList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_our_memory_friend_list;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_our_memory_friend_list, container, false);

        mPresenter = new FriendListPresenter();
        mPresenter.setView(this);

        EditText editText = view.findViewById(R.id.et_fragment_our_memory_friend_list_edit_text);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        mRecyclerView = view.findViewById(R.id.rv_fragment_our_memory_friend_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(container).getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), 1));

        ImageView addFriendButton = view.findViewById(R.id.btn_fragment_our_memory_friend_list_add_friend);
        //addFriendButton.setOnClickListener(view1 -> ((OurMemoryActivity) Objects.requireNonNull(getActivity())).startAddFriendActivity());

        ImageView searchFriendButton = view.findViewById(R.id.btn_fragment_our_memory_friend_list_search_friend);
        searchFriendButton.setOnClickListener(view1 -> editText.setVisibility(View.VISIBLE));

        ImageView settingButton = view.findViewById(R.id.btn_fragment_our_memory_friend_list_setting);
        settingButton.setOnClickListener(view1 -> showToast("친구 목록 설정 버튼"));

        return view;
    }

    @Override
    public void onDetach() {
        mPresenter.releaseView();

        super.onDetach();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getAppContext() {
        return Objects.requireNonNull(getActivity()).getApplicationContext();
    }

    @Override
    public void showFriendList(List<FriendPostResult.ResponseValue> responseValueList) {
        // 리사이클러뷰에 표시할 데이터 리스트 생성
        mFriendNameList = new ArrayList<>();
        mFriendIdList = new ArrayList<>();
        for (int i = 0; i < responseValueList.size(); i++) {
            mFriendNameList.add(responseValueList.get(i).getName());
            mFriendIdList.add(responseValueList.get(i).getFriendId());
        }

        // 리사이클러뷰에 FriendListAdapter 객체 지정
        FriendListAdapter adapter = new FriendListAdapter(mFriendNameList);
        mRecyclerView.setAdapter(adapter);
    }
}
