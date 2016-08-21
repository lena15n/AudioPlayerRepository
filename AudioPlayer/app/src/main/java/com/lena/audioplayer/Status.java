package com.lena.audioplayer;

/**
 * Created by Lena on 23.06.2016.
 */
public enum Status {
    IDLE("idle"),
    PLAYING("playing"),
    PAUSED("paused");

    private final String state;

    Status(String state) {
        this.state = state;
    }
}
