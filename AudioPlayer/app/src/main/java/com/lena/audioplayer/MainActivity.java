package com.lena.audioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";//файл настроек
    public static final String STATUS_STATE = "status";//храним

    public static final String IDLE_STRING = "idle";
    public static final String PLAYING_STRING = "playing";
    public static final String PAUSED_STRING = "paused";

    AudioPlayerService audioPlayerService;
    Intent intent;
    AudioPlayerService.LocalBinder binder;
    ServiceConnection serviceConnection;
    Status status;
    boolean bound = false;

    public final String LOG_TAG = "~~~Mine~~~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView statusLabel = (TextView) findViewById(R.id.statusTextView);
        final Button playButton = (Button) findViewById(R.id.playButton);

        intent = new Intent(MainActivity.this, AudioPlayerService.class);
        serviceConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder paramBinder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                binder = (AudioPlayerService.LocalBinder) paramBinder;
                audioPlayerService = binder.getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        
        if (playButton != null && statusLabel != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    /*if (!bound){
                        startService(intent);
                    }
                    else {*/

                    /*}*/

                    if (audioPlayerService != null) {
                        status = audioPlayerService.getStatus();


                        switch (status) {
                            case IDLE: {
                                audioPlayerService.play();
                                statusLabel.setText(R.string.status_playing);
                                playButton.setText(R.string.button_pause);
                            }
                            break;
                            case PLAYING: {
                                audioPlayerService.pause();
                                statusLabel.setText(R.string.status_paused);
                                playButton.setText(R.string.button_play);
                            }
                            break;
                            case PAUSED: {
                                audioPlayerService.play();
                                statusLabel.setText(R.string.status_playing);
                                playButton.setText(R.string.button_pause);
                            }
                            break;
                        }
                    }
                }
            });

            status = Status.IDLE;
            statusLabel.setText(R.string.status_idle);
            playButton.setText(R.string.button_play);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //sharedPreferences = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString(STATUS_STATE, status.getString());
        //editor.apply();
        //editor.apply();


    }

    @Override
    protected void onResume() {
        super.onResume();

        //Resources resources = getApplicationContext().getResources();
        //bindService(intent, serviceConnection, 0);
        //status = audioPlayerService.getStatus();



        final TextView statusLabel = (TextView) findViewById(R.id.statusTextView);
        final Button playButton = (Button) findViewById(R.id.playButton);

        if (statusLabel != null && playButton != null) {
            switch (status) {
                case IDLE: {
                    statusLabel.setText(R.string.status_idle);
                    playButton.setText(R.string.button_play);
                }
                break;
                case PLAYING: {
                    statusLabel.setText(R.string.status_playing);
                    playButton.setText(R.string.button_pause);
                }
                break;
                case PAUSED: {
                    statusLabel.setText(R.string.status_paused);
                    playButton.setText(R.string.button_play);
                }
                break;
                default: {
                    status = null;
                }
            }
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        // Unbind from the service
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
    }
}
