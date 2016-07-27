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
    private AudioPlayerService audioPlayerService;
    private Intent intent;
    private AudioPlayerService.LocalBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder paramBinder) {
            binder = (AudioPlayerService.LocalBinder) paramBinder;
            audioPlayerService = binder.getService();

            status = audioPlayerService.getStatus();
            Log.d(LOG_TAG, "status: " + status + ", AudioService: " + audioPlayerService);

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

        public void onServiceDisconnected(ComponentName name) {
            Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
        }
    };
    private Status status;
    private TextView statusLabel;
    private Button playButton;

    public final String LOG_TAG = "Mine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusLabel = (TextView) findViewById(R.id.statusTextView);
        playButton = (Button) findViewById(R.id.playButton);

        intent = new Intent(MainActivity.this, AudioPlayerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (playButton != null && statusLabel != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
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
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);
    }
}
