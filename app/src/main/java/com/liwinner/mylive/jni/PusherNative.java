package com.liwinner.mylive.jni;

import com.liwinner.mylive.listener.LiveStateChangeListener;

/**
 * Created by liwinner on 2017/12/11.
 */

public class PusherNative {
    public static final int CONNECT_FAILED = 101;
    public static final int INIT_FAILED = 102;

    static{
        System.loadLibrary("my-live");
    }
    private LiveStateChangeListener mLiveStateChangeListener;

    public void setLiveStateChangeListener(LiveStateChangeListener liveStateChangeListener) {
        mLiveStateChangeListener = liveStateChangeListener;
    }

    public void throwNativeError(int code) {
        if (mLiveStateChangeListener != null) {
            mLiveStateChangeListener.onError(code);
        }
    }

    public  native void startPush(String url);

    public  native void stopPush();

    public  native void release();

    public  native void setVideoOptions(int width,int height,int bitrate,int fps);

    public  native void setAudioOptions(int sampleRateInHz,int channel);

    public  native void fireVideo(byte[] data);

    public  native void fireAudio(byte[] data);

    public void removeLiveStateChangeListener() {
        mLiveStateChangeListener = null;
    }
}
