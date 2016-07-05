package com.lena.audioplayer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";//файл настроек
    public static final String STATUS_STATE = "status";//храним
    IBinder binder;

    SharedPreferences sharedPreferences;
    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        }
                        break;
                        case PLAYING: {
                            status = Status.PAUSED;
                            statusLabel.setText(R.string.status_paused);
                            playButton.setText(R.string.button_play);
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

        //записывать сюда еще и текущее время проигрывания музыки
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        String savedStatusValue = sharedPreferences.getString(STATUS_STATE, "Non status");// how to make it a constant from R class???


        final TextView statusLabel = (TextView) findViewById(R.id.statusTextView);
        final Button playButton = (Button) findViewById(R.id.playButton);

        if (!savedStatusValue.equals("Non status") && statusLabel != null && playButton != null) {
            switch (savedStatusValue) {
                case "idle": {
                    status = Status.IDLE;
                    statusLabel.setText(R.string.status_idle);
                    playButton.setText(R.string.button_play);
                }
                break;
                case "playing": {
                    status = Status.PLAYING;
                    statusLabel.setText(R.string.status_playing);
                    playButton.setText(R.string.button_pause);
                }
                break;
                case "paused": {
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
    }

        /*TextView textView = (TextView) findViewById(R.id.statusTextView);

        if (textView != null && status != null) {
            textView.setText(status.getString());
        }
*/

    // Intent intent = new Intent(this, AudioPlayerService.class);
    //startService(intent);


    //binder =  bindService(intent, );
}
