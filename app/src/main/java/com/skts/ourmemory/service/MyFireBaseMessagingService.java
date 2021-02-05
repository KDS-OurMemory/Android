package com.skts.ourmemory.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.skts.ourmemory.R;
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
        if (remoteMessage.getNotification() != null) {      // 포그라운드
            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();

            sendNotification(messageBody, messageTitle);
            DebugLog.d(TAG, "포그라운드 알림 메시지 : " + messageBody);
        } else if (remoteMessage.getData().size() > 0) {    //백그라운드
            String messageBody = remoteMessage.getData().get("body");
            String messageTitle = remoteMessage.getData().get("title");

            sendNotification(messageBody, messageTitle);
            DebugLog.d(TAG, "백그라운드 알림 메시지 : " + messageBody);
        }
    }

    /**
     * 포그라운드 및 백그라운드 푸시 알림 처리
     *
     * @param messageBody  : 메시지 내용
     * @param messageTitle : 메시지 타이틀
     */
    private void sendNotification(String messageBody, String messageTitle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

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
