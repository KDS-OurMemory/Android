package com.skts.ourmemory.view.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.AddToDoListContract;
import com.skts.ourmemory.presenter.AddToDoListPresenter;
import com.skts.ourmemory.view.BaseActivity;

import butterknife.BindView;

public class AddToDoListActivity extends BaseActivity implements AddToDoListContract.View {
    private AddToDoListContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_dialog_add_to_do_list_text)
    EditText mToDoEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_list);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.releaseView();
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void initSet() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        getWindow().getAttributes().height = (int) (displayMetrics.heightPixels * 0.7);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mPresenter = new AddToDoListPresenter();
        mPresenter.setView(this);
    }
}
