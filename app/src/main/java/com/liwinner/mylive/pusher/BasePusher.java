package com.liwinner.mylive.pusher;

/**
 * Created by liwinner on 2017/11/30.
 */

public abstract class BasePusher {
    public abstract void startPush();

    public abstract void stopPush();

    public abstract void release();
}
