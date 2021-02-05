package com.skts.ourmemory.view.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.skts.ourmemory.R;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.login.LoginActivity;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        DebugLog.e(TAG, "Fetching FCM registration token failed" + task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    DebugLog.d(TAG, "FCM 토큰 : " + token);
                });
    }
}