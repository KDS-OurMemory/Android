package com.skts.ourmemory.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.ToDoListContract;
import com.skts.ourmemory.model.todolist.EachToDoListPostResult;
import com.skts.ourmemory.model.todolist.ToDoListDAO;
import com.skts.ourmemory.model.todolist.ToDoListData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToDoListDialog extends Dialog {
    private final Context mContext;
    private AddToDoListDialogListener mAddListener;
    private EditToDoListDialogListener mEditListener;
    private final ToDoListContract.Presenter mPresenter;
    private final ToDoListData mToDoListData;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_dialog_to_do_list_date)
    TextView mDateText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_dialog_to_do_list_text)
    EditText mContent;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_dialog_to_do_list_delete)
    Button mDeleteBtn;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_dialog_to_do_list_save)
    Button mSaveBtn;

    public ToDoListDialog(Context context, ToDoListContract.Presenter presenter, ToDoListData toDoListData, AddToDoListDialogListener addToDoListDialogListener) {
        super(context);
        this.mContext = context;
        this.mPresenter = presenter;
        this.mToDoListData = toDoListData;
        this.mAddListener = addToDoListDialogListener;
    }

    public ToDoListDialog(Context context, ToDoListContract.Presenter presenter, ToDoListData toDoListData, EditToDoListDialogListener editToDoListDialogListener) {
        super(context);
        this.mContext = context;
        this.mPresenter = presenter;
        this.mToDoListData = toDoListData;
        this.mEditListener = editToDoListDialogListener;
    }

    public interface AddToDoListDialogListener {
        void saveBtn(int toDoId, String content, String date);
    }

    public interface EditToDoListDialogListener {
        void editBtn(String content, String date);

        void deleteBtn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_to_do_list);

        ButterKnife.bind(this);

        initSet();
    }

    private void initSet() {
        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 추가 버튼 클릭 시
        if (mToDoListData == null) {
            mSaveBtn.setText("등록");
            mDeleteBtn.setVisibility(View.GONE);

            // 처음 날짜
            GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜

            String today = simpleDateFormat.format(calendar.getTime());
            mDateText.setText(today);

            mDateSetListener = (datePicker, i, i1, i2) -> {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(i, i1, i2, 0, 0, 0);
                String date = simpleDateFormat.format(gregorianCalendar.getTime());
                mDateText.setText(date);
            };
        } else {
            // 편집
            mSaveBtn.setText("수정");
            mDeleteBtn.setVisibility(View.VISIBLE);

            mContent.setText(mToDoListData.getContent());

            mDateText.setText(mToDoListData.getDate());

            mDateSetListener = (datePicker, i, i1, i2) -> {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(i, i1, i2, 0, 0, 0);
                String date = simpleDateFormat.format(gregorianCalendar.getTime());
                mDateText.setText(date);
            };
        }
    }

    public void addTodoListResult(EachToDoListPostResult eachToDoListPostResult) {
        ToDoListDAO responseValue = eachToDoListPostResult.getResponse();

        // 데이터 넘기기
        mAddListener.saveBtn(responseValue.getTodoId(), responseValue.getContents(), responseValue.getTodoDate());
        dismiss();
    }

    public void putToDoListResult() {
        mEditListener.editBtn(mContent.getText().toString(), mDateText.getText().toString());
        dismiss();
    }

    public void deleteToDoListResult() {
        // 데이터 삭제
        mEditListener.deleteBtn();
        dismiss();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_dialog_to_do_list_calendar)
    void onClickCalendar() {
        // 추가 버튼 클릭 시
        if (mToDoListData == null) {
            GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
            new DatePickerDialog(mContext, mDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
        } else {
            // 편집
            try {
                Date date = simpleDateFormat.parse(mToDoListData.getDate());
                GregorianCalendar calendar = new GregorianCalendar();
                assert date != null;
                calendar.setTime(date);

                new DatePickerDialog(mContext, mDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_to_do_list_save)
    void onClickSave() {
        if (mContent.getText().length() == 0) {
            Toast.makeText(mContext, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 추가
        if (mToDoListData == null) {
            // 서버 통신
            mPresenter.setToDoListData(this, mContent.getText().toString(), mDateText.getText().toString());
        } else {
            // 수정
            mPresenter.putToDoListData(this, mToDoListData.getToDoListId(), mContent.getText().toString(), mDateText.getText().toString());
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_to_do_list_cancel)
    void onClickCancel() {
        dismiss();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_dialog_to_do_list_delete)
    void onClickDelete() {
        // 삭제
        mPresenter.deleteToDoListData(this, mToDoListData.getToDoListId());
    }
}
