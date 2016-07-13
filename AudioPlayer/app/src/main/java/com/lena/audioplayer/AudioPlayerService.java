package com.lena.audioplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final int NOTIFICATION_ID = 1;
    int mStartMode;       // indicates how to behave if the service is killed
    boolean mAllowRebind; // indicates whether onRebind should be used
    MediaPlayer mediaPlayer;//can play music and video
    NotificationManager notificationManager;
    Status status;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();// interface for clients that bind

    @Override
    public void onCreate() {
        // The service is being created

        status = Status.IDLE;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lately); // initialize it here

        MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(getPackageName(), String.format("Error(%s%s)", what, extra));
                String playlist = "ERROR";

                //restart() here;

                mediaPlayer.reset();

                return true;
            }
        };

        mediaPlayer.setOnErrorListener(onErrorListener);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()

        //Здесь создать новый отдельный поток!!! :

        //если скачиваем песню из интернета, то надо использовать:
        //      player.prepareAsync();
        //      player.start();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        //and WifiLock when work with the Internet

        //mediaPlayer.prepareAsync(); // prepare async to not block main thread, might take long! (for buffering, etc)

        //mp3 will be started after completion of preparing...
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
                status = Status.PLAYING;
            }
        });

        mStartMode = Service.START_STICKY;
        sendMyNotification();

        return mStartMode;
    }

    public void play() {
        mediaPlayer.start();
        status = Status.PLAYING;
    }

    public void pause() {
        mediaPlayer.pause();
        status = Status.PAUSED;
    }

    void sendMyNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

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
        notificationManager.notify(NOTIFICATION_ID, notification);


    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        AudioPlayerService getService() {
            // Return this instance of AudioPlayerService so clients can call public methods
            return AudioPlayerService.this;
        }
    }


    public Status getStatus() {
        return status;
    }

    // Called when MediaPlayer is ready
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        mediaPlayer.release();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!

        return false;
    }
}
