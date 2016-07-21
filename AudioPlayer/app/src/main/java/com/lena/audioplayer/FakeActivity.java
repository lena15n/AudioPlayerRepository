package com.lena.audioplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FakeActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPlayer(View v) {
        Intent i=new Intent(this, FakeMyAudioPlayer.class);

        i.putExtra(FakeMyAudioPlayer.EXTRA_PLAYLIST, "main");
        i.putExtra(FakeMyAudioPlayer.EXTRA_SHUFFLE, true);

        startService(i);
    }

    public void stopPlayer(View v) {
        stopService(new Intent(this, FakeMyAudioPlayer.class));
    }
}