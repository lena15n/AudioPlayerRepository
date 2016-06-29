package com.lena.audioplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

public class MyMediaBrowserServiceCompat extends MediaBrowserServiceCompat {
    private MediaSessionCompat mMediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        // Create your MediaSessionCompat.
        // You should already be doing this
        mMediaSession = new MediaSessionCompat(this,
                MyMediaBrowserServiceCompat.class.getSimpleName());
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setCallback(this);
        mMediaSession.setActive(true);
        // Make sure to configure your MediaSessionCompat as per
        // https://www.youtube.com/watch?v=FBC1FgWe5X4
        setSessionToken(mMediaSession.getSessionToken());
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid, Bundle rootHints) {
        // Returning null == no one can connect
        // so we’ll return something
        return new BrowserRoot(
                getString(R.string.app_name), // Name visible in Android Auto
                null); // Bundle of optional extras
    }

    @Override
    public void onLoadChildren(String parentId,
                               Result<List<MediaBrowserCompat.MediaItem>> result) {
        // I promise we’ll get to browsing
        result.sendResult(null);
    }

}
