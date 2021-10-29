package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.presenter.ToDoListPresenter;
import com.skts.ourmemory.util.AddToDoListDialog;

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

        mAdapter = new ToDoListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new ToDoListPresenter();
        mPresenter.setView(this);

        mBottomAppbar.setNavigationOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet_to_do_list);
            bottomSheetDialog.show();
        });
    }

    @Override
    public void getToDoListResult(ToDoListPostResult toDoListPostResult) {
        List<ToDoListPostResult.ResponseValue> responseValues = toDoListPostResult.getResponse();
        List<ToDoListData> toDoListData = new ArrayList<>();
        List<Integer> toDoIds = mPresenter.getSavedToDoListId();

        for (ToDoListPostResult.ResponseValue response : responseValues) {
            ToDoListData data;
            if (toDoIds.contains(response.getTodoId())) {
                // 내장 DB 에 toDoId가 있으면 -> 할 일 완료
                data = new ToDoListData(response.getTodoId(), response.getContents(), response.getTodoDate(), true);
            } else {
                data = new ToDoListData(response.getTodoId(), response.getContents(), response.getTodoDate(), false);
            }
            toDoListData.add(data);
        }
        List<Object> data = parseObjectData(toDoListData);

        mAdapter.setDataList(data);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnClickListener((view, position) -> {
            ToDoListData listData = mAdapter.setChecked(position);
            mAdapter.notifyItemChanged(position);

            // 내장 DB 저장
            mPresenter.setSQLiteData(listData);
        });
    }

    @Override
    public List<Object> parseObjectData(List<ToDoListData> listData) {
        List<Object> data = new ArrayList<>();

        for (int i = 0; i < listData.size(); i++) {
            String date = listData.get(i).getDate();
            if (!data.contains(date)) {
                data.add(date);
            }
            data.add(listData.get(i));
        }
        return data;
    }

    @Override
    public List<Object> addSetDate(ToDoListData toDoListData) {
        List<Object> data = new ArrayList<>();

        String date = toDoListData.getDate();
        data.add(date);
        data.add(toDoListData);

        return data;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fab_activity_to_do_list)
    void onClickAddToDoList() {
        AddToDoListDialog dialog = new AddToDoListDialog(this, mPresenter, (toDoId, content, date) -> {
            ToDoListData toDoListData = new ToDoListData(toDoId, content, date, false);

            List<Object> data = addSetDate(toDoListData);
            mAdapter.addItems(data);
        });
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_to_do_list_setting)
    void onClickSetting() {
        showToast("설정");
    }
}
