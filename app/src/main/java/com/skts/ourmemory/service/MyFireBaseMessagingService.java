package com.skts.ourmemory.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.main.MainActivity;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private final String TAG = MyFireBaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String token) {
        DebugLog.i(TAG, "Refreshed token : " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String dataType = "";

        if (remoteMessage.getNotification() != null) {
            // 포그라운드
            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();
            dataType = remoteMessage.getData().get(Const.DATA_TYPE);
            String dataString = remoteMessage.getData().get(Const.DATA_STRING);

            DebugLog.d(TAG, "포그라운드 알림 메시지 : " + messageBody + " " + messageTitle + " " + dataType + " " + dataString);
            sendNotification(messageBody, messageTitle, dataType, dataString);

        } else if (remoteMessage.getData().size() > 0) {
            // 백그라운드
            String messageBody = remoteMessage.getData().get("body");
            String messageTitle = remoteMessage.getData().get("title");
            dataType = remoteMessage.getData().get(Const.DATA_TYPE);
            String dataString = remoteMessage.getData().get(Const.DATA_STRING);

            DebugLog.d(TAG, "백그라운드 알림 메시지 : " + messageBody + " " + messageTitle + " " + dataType + " " + dataString);
            sendNotification(messageBody, messageTitle, dataType, dataString);
        }

        /*MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(getApplicationContext());

        if (dataType == null) {
            int alarmCount = mySharedPreferences.getIntExtra(Const.ALARM_COUNT);
            mySharedPreferences.putIntExtra(Const.ALARM_COUNT, alarmCount + 1);
        } else if (dataType.equals(ServerConst.FRIEND_REQUEST)) {
            // 친구 요청일 경우
            int friendCount = mySharedPreferences.getIntExtra(Const.FRIEND_REQUEST_COUNT);
            mySharedPreferences.putIntExtra(Const.FRIEND_REQUEST_COUNT, friendCount + 1);
        } else {
            int alarmCount = mySharedPreferences.getIntExtra(Const.ALARM_COUNT);
            mySharedPreferences.putIntExtra(Const.ALARM_COUNT, alarmCount + 1);
        }*/
    }

    /**
     * 포그라운드 및 백그라운드 푸시 알림 처리
     *
     * @param messageBody  : 메시지 내용
     * @param messageTitle : 메시지 타이틀
     */
    private void sendNotification(String messageBody, String messageTitle, String dataType, String dataString) {
        PendingIntent pendingIntent;
        Intent intent;

        if (dataType == null) {
            intent = new Intent(this, MainActivity.class);
        } else if (dataType.equals(ServerConst.FRIEND_REQUEST)) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Channel ID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.brain)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelName = "Channel Name";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}
