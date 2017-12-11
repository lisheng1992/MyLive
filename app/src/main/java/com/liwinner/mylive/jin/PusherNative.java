package com.liwinner.mylive.jin;

import com.liwinner.mylive.listener.LiveStateChangeListener;

/**
 * Created by liwinner on 2017/12/11.
 */

public class PusherNative {

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

    public  native void sendVideoData(byte[] data);

    public  native void sendAudioData(byte[] data);

    public void removeLiveStateChangeListener() {
        mLiveStateChangeListener = null;
    }
}
