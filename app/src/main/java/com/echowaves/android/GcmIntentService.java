package com.echowaves.android;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by dmitry on 10/15/14.
 *
 */
public class GcmIntentService extends IntentService {

    String mes;

    public GcmIntentService() {
        super("GcmMessageHandler");
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        String title = extras.getString("title");

        mes = extras.getString("message");

        generateNotification(this, mes);

        Log.i("GCM***************************", "Received : (" + messageType + ")  " + title + ":" + mes);

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }


    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        //activity which you want to open
        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentText(message)
                .setSmallIcon(icon)
                .setWhen(when)
                .setContentIntent(intent)
                .setContentTitle(context.getString(R.string.app_name))
                .build();

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("m", message);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);

    }


}