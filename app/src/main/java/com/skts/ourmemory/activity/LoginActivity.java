package com.skts.ourmemory.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.Session;
import com.skts.ourmemory.R;
import com.skts.ourmemory.sessionCallback.SessionCallback;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback = new SessionCallback();
    Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
