package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.SignUpContract;
import com.skts.ourmemory.presenter.SignUpPresenter;
import com.skts.ourmemory.view.main.MainActivity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {

    private final String TAG = SignUpActivity.class.getSimpleName();

    private SignUpPresenter mSignUpPresenter;

    /*다이얼로그*/
    AlertDialog alertDialog = null;
    ProgressDialog progressDialog;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_signUp_userName)
    EditText mEditUserName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_signUp_userName_description)
    TextView mDescUserName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.dp_activity_signUp_birthday)
    DatePicker mDpUserBirthday;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.radioGroup_activity_signUp_birthday_type)
    RadioGroup mRgUserBirthdayType;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.radioGroup_activity_signUp_birthday_open)
    RadioGroup mRgUserBirthdayOpen;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_activity_sign_up_join)
    Button mButtonSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        String userID = intent.getStringExtra(Const.USER_ID);
        String userName = intent.getStringExtra(Const.USER_NAME);
        String userBirthday = intent.getStringExtra(Const.USER_BIRTHDAY);
        int userLoginType = intent.getIntExtra(Const.USER_LOGIN_TYPE, 0);

        mSignUpPresenter = new SignUpPresenter(userID, userName, userBirthday, userLoginType);

        // presenter 와 연결
        mSignUpPresenter.setView(this);

        // presenter 초기 설정
        mSignUpPresenter.init();

        InputFilter[] inputFilters = new InputFilter[]{
                new InputFilter.LengthFilter(10), filterKor
        };

        mEditUserName.setFilters(inputFilters);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        // presenter 와의 연결을 해제합니다.
        mSignUpPresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_activity_sign_up_join)
    void onClickSignUp() {
        String userName = mEditUserName.getText().toString();
        int solarID = findViewById(R.id.radioBtn_activity_signUp_solar).getId();
        int publicID = findViewById(R.id.radioBtn_activity_signUp_public).getId();

        mSignUpPresenter.checkUserData(userName, mDpUserBirthday, mRgUserBirthdayType, mRgUserBirthdayOpen, solarID, publicID);
    }

    @Override
    public void setText(String text) {
        mEditUserName.setText(text);
    }

    @Override
    public void initBirthday(int birthdayYear, int birthdayMonth, int birthdayDayOfMonth) {
        mDpUserBirthday.init(birthdayYear, birthdayMonth, birthdayDayOfMonth, (datePicker, i, i1, i2) -> {
        });
    }

    @Override
    public void showAlertDialog() {
        String userBirthdayType, userBirthdayOpen, userBirthdayYear;
        if (mSignUpPresenter.mUserBirthdayType) {
            userBirthdayType = "양력";
        } else {
            userBirthdayType = "음력";
        }

        if (mSignUpPresenter.mUserBirthdayOpen) {
            userBirthdayOpen = "공개";
        } else {
            userBirthdayOpen = "비공개";
        }

        userBirthdayYear = String.valueOf(mDpUserBirthday.getYear());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setTitle("추가 정보 확인");
        alertDialog.setMessage(
                "이름 : " + mSignUpPresenter.mUserName + "\n" +
                        "생일 : " + userBirthdayYear + mSignUpPresenter.mUserBirthday + "\n" +
                        "생일 타입 : " + userBirthdayType + "\n" +
                        "생일 공개 여부 : " + userBirthdayOpen + "\n\n" +
                        "해당 정보가 맞습니까?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("회원 가입 진행 중...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
            progressDialog.show();

            // 백그라운드 실행
            mSignUpPresenter.serverTask();

        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    // 한글만 허용
    public InputFilter filterKor = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅣ가-힣]*$");

            if (!ps.matcher(source).matches()) {
                mDescUserName.setVisibility(View.VISIBLE);
                return "";
            }
            return null;
        }
    };
}
