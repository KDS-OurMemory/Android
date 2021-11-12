package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.EditMyPageContract;
import com.skts.ourmemory.presenter.EditMyPagePresenter;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class EditMyPageActivity extends BaseActivity implements EditMyPageContract.View {
    private EditMyPageContract.Presenter mPresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_edit_my_page)
    Toolbar mToolbar;               // Toolbar
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_edit_my_page_nickname)
    EditText mNickName;             // User name
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_edit_my_page_birthday)
    EditText mBirthday;             // User birthday
    @SuppressLint({"NonConstantResourceId", "UseSwitchCompatOrMaterialCode"})
    @BindView(R.id.switch_activity_edit_my_page_birthday_open)
    Switch mBirthdayOpen;           // User birthday open
    @SuppressLint({"NonConstantResourceId", "UseSwitchCompatOrMaterialCode"})
    @BindView(R.id.switch_activity_edit_my_page_birthday_solar)
    Switch mBirthdaySolar;          // User birthday solar
    @SuppressLint({"NonConstantResourceId", "UseSwitchCompatOrMaterialCode"})
    @BindView(R.id.switch_activity_edit_my_page_push_alarm)
    Switch mPushAlarm;              // Push alarm

    // 초기값 저장 변수
    private String mInitName;
    private String mInitBirthday;
    private boolean mInitBirthdayOpen;
    private boolean mInitBirthdaySolar;
    private boolean mInitPushAlarm;

    // Dialog
    AlertDialog mAlertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_page);

        initSet();      // 초기 설정
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

        mPresenter = new EditMyPagePresenter();
        mPresenter.setView(this);

        MySharedPreferences mySharedPreferences = mPresenter.getMySharedPreferences();

        mNickName.setText(mySharedPreferences.getStringExtra(Const.USER_NAME));
        mBirthday.setText(mySharedPreferences.getStringExtra(Const.USER_BIRTHDAY));
        mBirthdayOpen.setChecked(mySharedPreferences.getBooleanExtra(Const.USER_IS_BIRTHDAY_OPEN));
        mBirthdaySolar.setChecked(!mySharedPreferences.getBooleanExtra(Const.USER_IS_SOLAR));
        mPushAlarm.setChecked(mySharedPreferences.getBooleanExtra(Const.PUSH_ALARM));

        initValueSaved();       // 초기값 저장

        //editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mBirthday.setOnEditorActionListener((textView, action, keyEvent) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                return !checkBirthday();
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

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
    public void onBackPressed() {
        // 키보드 내리기
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mNickName.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(mBirthday.getWindowToken(), 0);

        if (isDataChanged()) {
            // 변화가 있을 경우
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mAlertDialog = builder.create();
            mAlertDialog.setTitle("변경 내용 저장");
            mAlertDialog.setMessage("저장 하시겠습니까?");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
                dialog.dismiss();
                // 생일 체크
                if (!checkBirthday()) {
                    return;
                }
                requestEditMyData();
            });
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), (dialog, which) -> {
                dialog.dismiss();
                finishView(false);
            });
            mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
            mAlertDialog.show();
            return;
        }

        super.onBackPressed();

        // 액티비티 전환 애니메이션 설정
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void initValueSaved() {
        mInitName = mNickName.getText().toString();
        mInitBirthday = mBirthday.getText().toString();
        mInitBirthdayOpen = mBirthdayOpen.isChecked();
        mInitBirthdaySolar = mBirthdaySolar.isChecked();
        mInitPushAlarm = mPushAlarm.isChecked();
    }

    /**
     * 변화값 체크
     *
     * @return true: 변화 O, false: 변화 X
     */
    @Override
    public boolean isDataChanged() {
        if (!mInitName.equals(mNickName.getText().toString())) {
            return true;
        } else if (!mInitBirthday.equals(mBirthday.getText().toString())) {
            return true;
        } else if (!(mInitBirthdayOpen == mBirthdayOpen.isChecked())) {
            return true;
        } else if (!(mInitBirthdaySolar == mBirthdaySolar.isChecked())) {
            return true;
        } else return !(mInitPushAlarm == mPushAlarm.isChecked());
    }

    @Override
    public boolean checkBirthday() {
        if (mBirthday.getText().length() < 4) {
            // 4글자 미만일 경우
            showToast("생일 입력 양식에 맞춰 작성해주세요. (4글자)");
            return false;
        }
        return true;
    }

    @Override
    public void requestEditMyData() {
        String name = mNickName.getText().toString();
        String birthday = mBirthday.getText().toString();
        boolean birthdayOpen = mBirthdayOpen.isChecked();
        boolean birthdaySolar = !mBirthdaySolar.isChecked();
        boolean pushAlarm = mPushAlarm.isChecked();

        mPresenter.editMyData(name, birthday, birthdaySolar, birthdayOpen, pushAlarm);
    }

    @Override
    public void finishView(boolean changed) {
        Intent intent = new Intent();
        if (changed) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(Const.RESULT_FAIL, intent);
        }

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 확인 버튼
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_edit_my_page_confirm)
    void onClickConfirm() {
        try {
            // 오류 체크 용
            Integer.parseInt(mBirthday.getText().toString());

            // 변화 값 체크
            if (!isDataChanged()) {
                finishView(false);
                return;
            }

            // 생일 체크
            if (!checkBirthday()) {
                return;
            }
            requestEditMyData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("올바른 값을 입력해주세요.");
        }
    }
}
