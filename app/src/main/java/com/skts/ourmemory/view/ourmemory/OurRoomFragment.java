package com.skts.ourmemory.view.ourmemory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.RoomListAdapter;
import com.skts.ourmemory.contract.OurRoomContract;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.presenter.OurRoomPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OurRoomFragment extends BaseFragment implements OurRoomContract.View {
    private OurRoomContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;

    public OurRoomFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_our_memory_room_list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_our_memory_room_list, container, false);

        mPresenter = new OurRoomPresenter();
        mPresenter.setView(this);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        mRecyclerView = view.findViewById(R.id.rv_fragment_our_memory_room_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(container).getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), 1));

        ImageView imageView = view.findViewById(R.id.btn_fragment_our_memory_room_list_add_friend);
        imageView.setOnClickListener(view1 -> ((OurMemoryActivity) Objects.requireNonNull(getActivity())).startAddRoomActivity());

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
    public void showRoomList(List<RoomPostResult.ResponseValue> responseValueList) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> participants = new ArrayList<>();
        ArrayList<List<RoomPostResult.Member>> membersList = new ArrayList<>();

        for (int i = 0; i < responseValueList.size(); i++) {
            names.add(responseValueList.get(i).getName());
            membersList.add(responseValueList.get(i).getMemberList());
        }

        for (int i = 0; i < names.size(); i++) {
            StringBuilder members = new StringBuilder();
            for (int j = 0; j < membersList.get(i).size(); j++) {
                members.append(membersList.get(i).get(j).getName()).append(" ");
            }
            participants.add(members.toString());
        }

        RoomListAdapter roomListAdapter = new RoomListAdapter(names, participants);
        mRecyclerView.setAdapter(roomListAdapter);
    }
}
