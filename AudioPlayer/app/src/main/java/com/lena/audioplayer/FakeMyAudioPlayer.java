package com.lena.audioplayer;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import android.app.Notification;
import android.app.PendingIntent;
import android.util.Log;

public class FakeMyAudioPlayer extends Service {
    public static final String EXTRA_PLAYLIST="EXTRA_PLAYLIST";
    public static final String EXTRA_SHUFFLE="EXTRA_SHUFFLE";
    private boolean isPlaying=false;
    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String playlist=intent.getStringExtra(EXTRA_PLAYLIST);
        boolean useShuffle=intent.getBooleanExtra(EXTRA_SHUFFLE, false);

        play(playlist, useShuffle);

        return(START_NOT_STICKY);
    }

    @Override
    public void onDestroy() {
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return(null);
    }

    private void play(String playlist, boolean useShuffle) {
        if (!isPlaying) {
            Log.w(getClass().getName(), "Got to play()!");
            isPlaying=true;

            Context context = getApplicationContext();
            Notification.Builder builder = new Notification.Builder(getApplicationContext());
            Resources resouces = context.getResources();
            String songName = resouces.getString(R.string.songname);
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);


            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.cat_icon)
                    // большая картинка
                    .setLargeIcon(BitmapFactory.decodeResource(resouces, R.drawable.large_cat_icon))
                    //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true) // уведомление исчезает как только пользователь касается его
                    .setContentTitle(resouces.getString(R.string.notifytitle)) // Заголовок уведомления
                    .setContentText(resouces.getString(R.string.notifytext) + songName) // Текст уведомления
                    .setTicker(resouces.getString(R.string.tickertext)); // Текст бегушей строки


            // Notification notification = builder.getNotification(); // до API 16
            Notification notification = builder.build();

            // в эмуляторе не работает, на устройствах - не всегда, зависит от них самих
            notification.ledARGB = 0xff0000ff;//задать их цвет
            notification.ledOffMS = 0;//включить светодиоды
            notification.ledOnMS = 1;
            notification.flags = notification.flags | Notification.FLAG_SHOW_LIGHTS;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(NOTIFICATION_ID, notification);

            startForeground(1337, notification);
        }
    }

    private void stop() {
        if (isPlaying) {
            Log.w(getClass().getName(), "Got to stop()!");
            isPlaying=false;
            stopForeground(true);
        }
    }
}
