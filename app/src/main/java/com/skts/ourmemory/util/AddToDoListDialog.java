package com.skts.ourmemory.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skts.ourmemory.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddToDoListDialog extends Dialog {
    private final Context mContext;
    private final AddToDoListDialogListener mListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_dialog_add_to_do_list_date)
    TextView mDateText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_dialog_add_to_do_list_text)
    EditText mContent;

    public AddToDoListDialog(Context context, AddToDoListDialogListener addToDoListDialogListener) {
        super(context);
        this.mContext = context;
        this.mListener = addToDoListDialogListener;
    }

    public interface AddToDoListDialogListener {
        void saveBtn(String content, String date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_to_do_list);

        ButterKnife.bind(this);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 처음 날짜
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜

        String today = simpleDateFormat.format(calendar.getTime());
        mDateText.setText(today);

        mDateSetListener = (datePicker, i, i1, i2) -> {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(i, i1, i2, 0, 0, 0);
            String date = simpleDateFormat.format(gregorianCalendar.getTime());
            mDateText.setText(date);
        };
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_dialog_add_to_do_list_calendar)
    void onClickCalendar() {
        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        new DatePickerDialog(mContext, mDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_add_to_do_list_save)
    void onClickSave() {
        if (mContent.getText().length() == 0) {
            Toast.makeText(mContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 데이터 넘기기
        mListener.saveBtn(mContent.getText().toString(), mDateText.getText().toString());
        dismiss();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_add_to_do_list_cancel)
    void onClickCancel() {
        dismiss();
    }
}
