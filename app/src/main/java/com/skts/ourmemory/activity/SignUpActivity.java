package com.skts.ourmemory.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.UserModel;
import com.skts.ourmemory.util.DebugLog;

public class SignUpActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        UserModel userModel = (UserModel) intent.getSerializableExtra("UserModel");

        DebugLog.e(TAG, "id: " + userModel.getId() + ", 이름: " + userModel.getName() + ", 생일: " + userModel.getBirthday()
                + ", 생일공개여부: " + userModel.getBirthdayOpen() + ", 로그인 유형: " + userModel.getLoginType() + ", 가입날짜: " + userModel.getJoinDate());
    }
}
