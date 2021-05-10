package com.skts.ourmemory.view.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.HomeRoomAdapter;
import com.skts.ourmemory.contract.HomeContract;
import com.skts.ourmemory.model.main.HomeRoomData;
import com.skts.ourmemory.model.main.HomeRoomPostResult;
import com.skts.ourmemory.presenter.HomePresenter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {
    private HomeRoomAdapter mHomeRoomAdapter;
    private final HomeContract.Presenter mPresenter;
    private Context mContext;

    public HomeFragment() {
        mPresenter = new HomePresenter();
    }

    /**
     * setHasOptionsMenu true : 액티비티보다 프레그먼트의 메뉴가 우선
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_main_home_recyclerview);

        // Init layoutManager
        assert container != null;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Set layoutManger
        recyclerView.setLayoutManager(linearLayoutManager);

        mHomeRoomAdapter = new HomeRoomAdapter();
        recyclerView.setAdapter(mHomeRoomAdapter);

        mContext = container.getContext();

        mPresenter.setView(this);

        showRoomList(mContext);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mContext = null;
        mPresenter.releaseView();
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRoomList(Context context) {
        mPresenter.getRoomList(context);

        /*HomeRoomData homeRoomData = new HomeRoomData("테스트", "오광석");
        HomeRoomData homeRoomData2 = new HomeRoomData("테스트2", "오광석2");
        mHomeRoomAdapter.addItem(homeRoomData);
        mHomeRoomAdapter.addItem(homeRoomData2);*/
    }

    @Override
    public void addRoomList(ArrayList<String> names, List<List<HomeRoomPostResult.Member>> membersList) {
        for (int i = 0; i < names.size(); i++) {
            StringBuilder members = new StringBuilder();
            for (int j = 0; j < membersList.get(i).size(); j++) {
                members.append(membersList.get(i).get(j).getName()).append(" ");
            }
            HomeRoomData homeRoomData = new HomeRoomData(names.get(i), members.toString());
            mHomeRoomAdapter.addItem(homeRoomData);
        }
    }
}
