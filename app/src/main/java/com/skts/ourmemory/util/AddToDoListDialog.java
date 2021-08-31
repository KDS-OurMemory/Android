package com.skts.ourmemory.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skts.ourmemory.R;

import java.util.Objects;

public class AddToDoListDialog extends Dialog {
    private final Context mContext;
    private final AddToDoListDialogListener mListener;

    public AddToDoListDialog(Context context, AddToDoListDialogListener addToDoListDialogListener) {
        super(context);
        this.mContext = context;
        this.mListener = addToDoListDialogListener;
    }

    public interface AddToDoListDialogListener {
        void saveBtn(String content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_to_do_list);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        EditText content = findViewById(R.id.et_dialog_add_to_do_list_text);
        Button saveButton = findViewById(R.id.btn_dialog_add_to_do_list_save);
        Button cancelButton = findViewById(R.id.btn_dialog_add_to_do_list_cancel);

        // 등록 버튼
        saveButton.setOnClickListener(view -> {
            if (content.getText().length() == 0) {
                Toast.makeText(mContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 데이터 넘기기
            mListener.saveBtn(content.getText().toString());
            dismiss();
        });

        // 취소 버튼
        cancelButton.setOnClickListener(view -> dismiss());
    }
}
