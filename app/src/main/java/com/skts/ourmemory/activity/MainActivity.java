package com.skts.ourmemory.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.skts.ourmemory.R;
import com.skts.ourmemory.model.UserModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        UserModel userModel = (UserModel) intent.getSerializableExtra("UserModel");

        Log.d("testttt", "이름 : " + userModel.getName());
        Log.d("testttt", "핸드폰 : " + userModel.getMobile());
    }
}