package com.lena.audioplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";//файл настроек
    public static final String STATUS_STATE = "status";//храним

    public static final String IDLE_STRING = "idle";
    public static final String PLAYING_STRING = "playing";
    public static final String PAUSED_STRING = "paused";
    IBinder binder;

    SharedPreferences sharedPreferences;
    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        sharedPreferences.edit().remove(STATUS_STATE).apply();
        // or Settings -> Applications -> Manage applications -> (choose your app) -> Clear data or Uninstall


        final TextView statusLabel = (TextView) findViewById(R.id.statusTextView);
        final Button playButton = (Button) findViewById(R.id.playButton);



        if (playButton != null && statusLabel != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch (status) {
                        case IDLE: {
                            status = Status.PLAYING;
                            statusLabel.setText(R.string.status_playing);
                            playButton.setText(R.string.button_pause);

                            //первый запуск сервиса
                            startService(new Intent(MainActivity.this, AudioPlayerService.class));
                        }
                        break;
                        case PLAYING: {
                            status = Status.PAUSED;
                            statusLabel.setText(R.string.status_paused);
                            playButton.setText(R.string.button_play);

                           // binder = bindService()
                        }
                        break;
                        case PAUSED: {
                            status = Status.PLAYING;
                            statusLabel.setText(R.string.status_playing);
                            playButton.setText(R.string.button_pause);
                        }
                        break;
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

        sharedPreferences = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STATUS_STATE, status.getString());
        //editor.apply();
        editor.apply();


    }

    @Override
    protected void onResume() {
        super.onResume();

        Resources resources = getApplicationContext().getResources();
        //записывать сюда еще и текущее время проигрывания музыки
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        String savedStatusValue = sharedPreferences.getString(STATUS_STATE, resources.getString(R.string.non_status));


        final TextView statusLabel = (TextView) findViewById(R.id.statusTextView);
        final Button playButton = (Button) findViewById(R.id.playButton);

        if (!savedStatusValue.equals(resources.getString(R.string.non_status)) &&
                                    statusLabel != null && playButton != null) {
            switch (savedStatusValue) {
                case IDLE_STRING: {
                    status = Status.IDLE;
                    statusLabel.setText(R.string.status_idle);
                    playButton.setText(R.string.button_play);
                }
                break;
                case PLAYING_STRING: {
                    status = Status.PLAYING;
                    statusLabel.setText(R.string.status_playing);
                    playButton.setText(R.string.button_pause);
                }
                break;
                case PAUSED_STRING: {
                    status = Status.PAUSED;
                    statusLabel.setText(R.string.status_paused);
                    playButton.setText(R.string.button_play);
                }
                break;
                default: {
                    status = null;
                }
            }
        }

        //binder =  bindService(intent, );
    }

}
