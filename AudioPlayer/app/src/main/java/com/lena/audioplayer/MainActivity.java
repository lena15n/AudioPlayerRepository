package com.lena.audioplayer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "mysettings";//файл настроек
    public static final String STATUS_STATE = "status";//храним

    SharedPreferences sharedPreferences;
    Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = Status.IDLE;


    }

    @Override
    protected void onPause() {
        super.onPause();

        sharedPreferences = this.getSharedPreferences("status", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(STATUS_STATE, status.getString());
        editor.apply();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (status == null) {
            sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
            String savedStatusValue = sharedPreferences.getString(STATUS_STATE, "Non Status");


            switch (savedStatusValue) {
                case "idle": {
                    status = Status.IDLE;
                }
                break;
                case "playing": {
                    status = Status.PLAYING;
                }
                break;
                case "paused": {
                    status = Status.PAUSED;
                }
                break;
                default: {
                    status = null;
                }
            }
        }

        TextView textView = (TextView) findViewById(R.id.statusTextView);

        if (textView != null && status != null) {
            textView.setText(status.getString());
        }




    }
}
