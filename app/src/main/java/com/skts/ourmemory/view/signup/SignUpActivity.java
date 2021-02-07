package com.skts.ourmemory.view.signup;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.skts.ourmemory.R;
import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.model.ReceiveUserModel;
import com.skts.ourmemory.model.SendUserModel;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.util.DebugLog;

import java.util.Calendar;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = SignUpActivity.class.getSimpleName();

    /*사용자 정보*/
    private String mUserID;             // id
    private String mUserName;           // 이름(=별명)
    private String mUserBirthday;       // 생일
    private boolean mUserBirthdayType;  // 양/음력
    private boolean mUserBirthdayOpen;  // 생일 공개 여부
    private int mUserLoginType;         // 로그인 형식(1: 카카오, 2: 구글, 3: 네이버)

    /*Retrofit*/
    private Retrofit retrofit;
    private IRetrofitApi retrofitApi;

    private EditText mEditUserName;
    private TextView mDescUserName;
    private DatePicker mDpUserBirthday;
    private RadioGroup mRgUserBirthdayType;
    private RadioGroup mRgUserBirthdayOpen;
    private Button mButtonSignUp;

    /*RxJava*/
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /*다이얼로그*/
    AlertDialog alertDialog = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        mUserID = intent.getStringExtra(Const.USER_ID);
        mUserName = intent.getStringExtra(Const.USER_NAME);
        mUserBirthday = intent.getStringExtra(Const.USER_BIRTHDAY);
        mUserLoginType = intent.getIntExtra(Const.USER_LOGIN_TYPE, 0);

        DebugLog.i(TAG, "아이디 : " + mUserID + ", 이름 : " + mUserName + ", 생일 : " + mUserBirthday + ", 로그인 유형 : " + mUserLoginType);

        retrofit = new Retrofit.Builder()
                .baseUrl(ServerConst.TEST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitApi = retrofit.create(IRetrofitApi.class);

        mEditUserName = findViewById(R.id.et_activity_signUp_userName);
        mDescUserName = findViewById(R.id.tv_activity_signUp_userName_description);
        mDpUserBirthday = findViewById(R.id.dp_activity_signUp_birthday);
        mRgUserBirthdayType = findViewById(R.id.radioGroup_activity_signUp_birthday_type);
        mRgUserBirthdayOpen = findViewById(R.id.radioGroup_activity_signUp_birthday_open);
        mButtonSignUp = findViewById(R.id.btn_activity_sign_up_join);

        InputFilter[] inputFilters = new InputFilter[]{
                new InputFilter.LengthFilter(10),
                filterKor
        };
        mEditUserName.setFilters(inputFilters);

        if (mUserName != null) {
            mEditUserName.setText(mUserName);
        }

        if (mUserBirthday != null) {
            if (mUserBirthday.contains("-")) {
                //네이버는 - 포함, "-" 제거
                mUserBirthday = mUserBirthday.replaceAll("-", "");
            }
            final Calendar calendar = Calendar.getInstance();
            int birthdayYear = calendar.get(Calendar.YEAR);
            int birthdayMonth = Integer.parseInt(mUserBirthday.substring(0, 2)) - 1;        // 월은 -1 해줘야 함
            int birthdayDayOfMonth = Integer.parseInt(mUserBirthday.substring(2));
            mDpUserBirthday.init(birthdayYear, birthdayMonth, birthdayDayOfMonth, (datePicker, i, i1, i2) -> {
            });
        }

        mButtonSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_sign_up_join:
                checkUserData();
                break;
        }
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

        mCompositeDisposable.dispose();
    }

    private void checkUserData() {
        mUserName = mEditUserName.getText().toString();
        if (mUserName.equals("")) {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mUserBirthday = String.format("%02d", mDpUserBirthday.getMonth() + 1);
        mUserBirthday += String.format("%02d", mDpUserBirthday.getDayOfMonth());
        if (mRgUserBirthdayType.getCheckedRadioButtonId() == (findViewById(R.id.radioBtn_activity_signUp_solar)).getId()) {
            // 양력
            mUserBirthdayType = true;
        } else {
            // 음력
            mUserBirthdayType = false;
        }
        if (mRgUserBirthdayOpen.getCheckedRadioButtonId() == (findViewById(R.id.radioBtn_activity_signUp_public).getId())) {
            // 공개
            mUserBirthdayOpen = true;
        } else {
            // 비공개
            mUserBirthdayOpen = false;
        }
        showAlertDialog();
    }

    private void showAlertDialog() {
        String userBirthdayType, userBirthdayOpen, userBirthdayYear;
        if (mUserBirthdayType) {
            userBirthdayType = "양력";
        } else {
            userBirthdayType = "음력";
        }

        if (mUserBirthdayOpen) {
            userBirthdayOpen = "공개";
        } else {
            userBirthdayOpen = "비공개";
        }

        userBirthdayYear = String.valueOf(mDpUserBirthday.getYear());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setTitle("추가 정보 확인");
        alertDialog.setMessage(
                "이름 : " + mUserName + "\n" +
                        "생일 : " + userBirthdayYear + mUserBirthday + "\n" +
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
            serverTask();

        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void serverTask() {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        //SendUserModel sendUserModel = new SendUserModel(mUserID, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType);
        SendUserModel sendUserModel = new SendUserModel(mUserID, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType, ServerConst.FIREBASE_PUSH_TOKEN);
        Observable<ReceiveUserModel> observable = service.postData(sendUserModel);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ReceiveUserModel>() {
                                   @Override
                                   public void onNext(@NonNull ReceiveUserModel receiveUserModel) {
                                       DebugLog.i(TAG, receiveUserModel.toString());
                                       if (progressDialog != null) {
                                           progressDialog.dismiss();
                                       }
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       if (progressDialog != null) {
                                           progressDialog.dismiss();
                                       }
                                       Toast.makeText(SignUpActivity.this, "서버와 통신이 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "성공");
                                       if (progressDialog != null) {
                                           progressDialog.dismiss();
                                       }
                                   }
                               }

                ));
    }

    /*private void serverTask() {
        SendUserModel sendUserModel = new SendUserModel(mUserID, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType);
        retrofitApi.postData(sendUserModel).enqueue(new Callback<ReceiveUserModel>() {
            @Override
            public void onResponse(Call<ReceiveUserModel> call, Response<ReceiveUserModel> response) {
                if (response.isSuccessful()) {
                    DebugLog.i(TAG, "성공!");
                    ReceiveUserModel data = response.body();
                    DebugLog.i(TAG, "결과 : " + data.getResult() + ", 가입 날짜 : " + data.getJoinTime());

                    if (data.getResult() == 1) {
                        //입력 정보 오류

                    } else if (data.getResult() == 2) {
                        //기존 가입 회원

                    } else {        //data.getResult() == 0
                        //성공
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReceiveUserModel> call, Throwable t) {
                DebugLog.i(TAG, "실패!");
            }
        });
    }*/

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

    private void BackgroundTask(String Url) {
        //onPreExecute


        //doInBackground


        //onPostExecute
    }
}
