package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.ToDoListAdapter;
import com.skts.ourmemory.contract.ToDoListContract;
import com.skts.ourmemory.model.todolist.ToDoListData;
import com.skts.ourmemory.presenter.ToDoListPresenter;
import com.skts.ourmemory.view.todolist.AddToDoListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class ToDoListActivity extends BaseActivity implements ToDoListContract.View {
    private ToDoListContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_to_do_list)
    MaterialToolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottom_appbar_activity_to_do_list)
    BottomAppBar mBottomAppbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_to_do_list)
    RecyclerView mRecyclerView;

    private ToDoListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        initSet();
        setRecycler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void initSet() {
        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mPresenter = new ToDoListPresenter();
        mPresenter.setView(this);

        mBottomAppbar.setNavigationOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);
            bottomSheetDialog.show();
        });
    }

    @Override
    public void setRecycler() {
        mAdapter = new ToDoListAdapter();
        List<ToDoListData> tempData = tempSetData();
        List<Object> list = tempSetDate(tempData);

        mAdapter.setDataList(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnClickListener((view, position) -> {
            mAdapter.setChecked(position);
            mAdapter.notifyItemChanged(position);
        });
    }

    public List<ToDoListData> tempSetData() {
        List<ToDoListData> list = new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            String content = i+"번째 할 일";
            String date = "2021-08-2"+i;
            ToDoListData data = new ToDoListData(false, content, date);
            list.add(data);
        }*/
        ToDoListData data1 = new ToDoListData(false, "물 2L 마시기", "2021-08-20");
        ToDoListData data2 = new ToDoListData(false, "저녁 식사", "2021-08-20");
        ToDoListData data3 = new ToDoListData(false, "일정 목록 조회 수정", "2021-08-20");
        ToDoListData data4 = new ToDoListData(false, "축구 경기 보기", "2021-08-21");
        ToDoListData data5 = new ToDoListData(false, "To Do List 개발", "2021-08-21");
        ToDoListData data6 = new ToDoListData(false, "물 2L 마시기", "2021-08-23");
        ToDoListData data7 = new ToDoListData(false, "독서하기", "2021-08-24");
        ToDoListData data8 = new ToDoListData(false, "게임", "2021-08-25");
        ToDoListData data9 = new ToDoListData(false, "운동", "2021-08-25");

        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);
        list.add(data7);
        list.add(data8);
        list.add(data9);

        return list;
    }

    public List<Object> tempSetDate(List<ToDoListData> list) {
        List<Object> data = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String date = list.get(i).getDate();
            if (!data.contains(date)) {
                data.add(date);
            }
            data.add(list.get(i));
        }
        return data;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fab_activity_to_do_list)
    void onClickAddToDoList() {
        startActivity(new Intent(this, AddToDoListActivity.class));
    }
}
