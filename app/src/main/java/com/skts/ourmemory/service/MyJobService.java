package com.skts.ourmemory.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyJobService extends Service {
    private final String TAG = MyJobService.class.getSimpleName();

    public MyJobService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * @param intent  intent
     * @param flags   플래그
     * @param startId startId
     * @return START_STICKY : 서비스 재생성 후 onStartCommand() 다시 호출. 강제 종료된 후의 마지막 Intent 는 다시 호출하지 않음
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
