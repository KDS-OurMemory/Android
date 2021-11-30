package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.ItemTouchHelperCallback;
import com.skts.ourmemory.adapter.RoomListAdapter;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.presenter.OurMemoryPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OurMemoryFragment extends BaseFragment implements OurMemoryContract.View {
    private Unbinder unbinder;
    private OurMemoryContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_fragment_main_our_memory)
    Toolbar mToolbar;       // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_main_our_memory_recyclerview)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_fragment_our_memory_room_list_edit_text)
    EditText mSearchText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_our_memory_no_room_text)
    TextView mNoRoomText;

    private RoomListAdapter mRoomListAdapter;

    public OurMemoryFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_our_memory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_our_memory, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mPresenter = new OurMemoryPresenter();
        mPresenter.setView(this);

        initView(rootView);
        initSet();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mPresenter.releaseView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
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
    public void initView(View view) {
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("");
    }

    @Override
    public void initSet() {
        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getAppContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getAppContext(), 1));

        mRoomListAdapter = new RoomListAdapter();
        mRecyclerView.setAdapter(mRoomListAdapter);

        if (((MainActivity) Objects.requireNonNull(getActivity())).getRoomData() != null) {
            showRoomData(((MainActivity) getActivity()).getRoomData());
        }
    }

    @Override
    public void showNoRoom(boolean status) {
        if (status) {
            mNoRoomText.setVisibility(View.VISIBLE);
        } else {
            mNoRoomText.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRoomData(List<RoomResponseValue> responseValues) {
        mRoomListAdapter = new RoomListAdapter(responseValues);
        mRecyclerView.setAdapter(mRoomListAdapter);
        mRoomListAdapter.setRecycler(mRecyclerView);

        // ItemTouchHelper 생성
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(mRoomListAdapter));

        // RecyclerView 에 ItemTouchHelper 붙이기
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRoomListAdapter.setOnItemClickListener((view, position) -> {
            // 중복 클릭 방지
            ((MainActivity) Objects.requireNonNull(getActivity())).startRoomActivity(position);
        });

        // 방 삭제
        mRoomListAdapter.setOnItemDeleteClickListener(position -> ((MainActivity) Objects.requireNonNull(getActivity())).deleteRoomData(position));
    }

    //public void updateCalendarData

    /*@Override
    public void updateCalendarData(MemoryDAO memoryDAO, String mode) {
        //mDescriptionDown.performClick();        // 설명 레이아웃 닫기

        if (mode.equals(Const.CALENDAR_ADD) || mode.equals(Const.CALENDAR_ADD_AND_SHARE)) {
            showToast(memoryDAO.getName() + " 일정이 추가되었습니다");
            if (mAdapter.isLayoutFoldStatus()) {
                // 접혀 있을 때
                Calendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay());
                mDescriptionAdapter.addItem(memoryDAO, calendar);     // 설명 창
            }
            mAdapter.addPlusItem(memoryDAO);

            // 설명 레이아웃에 일정 여부 있는지 확인
            if (mDescriptionAdapter.getItemCount() == 0) {
                mNoCalendarText.setVisibility(View.VISIBLE);
            } else {
                mNoCalendarText.setVisibility(View.GONE);
            }
        } else if (mode.equals(Const.CALENDAR_EDIT) || mode.equals(Const.CALENDAR_EDIT_AND_SHARE)) {
            showToast(memoryDAO.getName() + " 일정이 수정되었습니다");
            Calendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay());
            mDescriptionAdapter.editItem(memoryDAO, calendar);        // 설명 창
            mAdapter.editItem(memoryDAO);
        } else {
            // 일정 삭제
            showToast(memoryDAO.getName() + " 일정이 삭제되었습니다");
            mDescriptionAdapter.deleteItem(memoryDAO.getMemoryId());
            mAdapter.deleteItem(memoryDAO.getMemoryId());

            if (mDescriptionAdapter.getItemCount() == 0) {
                mNoCalendarText.setVisibility(View.VISIBLE);
            }
        }
    }*/

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_room_list_search_friend)
    void onClickSearchRoomBtn() {
        if (mSearchText.getVisibility() == View.VISIBLE) {
            mSearchText.setVisibility(View.GONE);
        } else {
            mSearchText.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_room_list_add_friend)
    void onClickAddRoomBtn() {
        ((MainActivity) Objects.requireNonNull(getActivity())).startAddRoomActivity();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_our_memory_room_list_setting)
    void onClickSettingBtn() {
        showToast("방 목록 설정 버튼");
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tv_fragment_main_our_memory_focus_up_btn)
    void onClickFocusUpBtn() {
        mRecyclerView.smoothScrollToPosition(0);
    }
}
