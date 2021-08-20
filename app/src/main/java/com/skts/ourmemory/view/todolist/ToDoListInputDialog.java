package com.skts.ourmemory.view.todolist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.skts.ourmemory.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToDoListInputDialog extends Dialog {
    public ToDoListInputDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_to_do_list);
        ButterKnife.bind(this);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_add_to_do_list_cancel)
    void onClickCancelBtn() {
        dismiss();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_add_to_do_list_enroll)
    void onClickEnrollBtn() {
        dismiss();
    }
}
