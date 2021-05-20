package com.skts.ourmemory.view.ourmemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.contract.FriendListContract;
import com.skts.ourmemory.presenter.FriendListPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.Objects;

public class FriendListFragment extends BaseFragment implements FriendListContract.View {
    private FriendListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;

    public FriendListFragment() {
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
        // 친구목록 조회
        mPresenter.getFriendList();

        EditText editText = view.findViewById(R.id.et_fragment_our_memory_friend_list_edit_text);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        mRecyclerView = view.findViewById(R.id.rv_fragment_our_memory_friend_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        Button addFriendButton = view.findViewById(R.id.btn_fragment_our_memory_friend_list_add_friend);
        addFriendButton.setOnClickListener(view1 -> ((OurMemoryActivity) Objects.requireNonNull(getActivity())).startAddFriendActivity());

        return view;
    }

    @Override
    public void onDetach() {
        mPresenter.releaseView();

        super.onDetach();
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFriendList(ArrayList<Integer> userIds, ArrayList<String> names) {
        // 리사이클러뷰에 표시할 데이터 리스트 생성
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            list.add(names.get(i));
        }

        // 리사이클러뷰에 FriendListAdapter 객체 지정
        FriendListAdapter adapter = new FriendListAdapter(list);
        mRecyclerView.setAdapter(adapter);
    }
}
