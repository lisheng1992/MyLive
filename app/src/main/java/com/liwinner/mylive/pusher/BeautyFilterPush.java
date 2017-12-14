package com.liwinner.mylive.pusher;

import com.liwinner.mylive.jni.PusherNative;
import com.liwinner.mylive.listener.LiveStateChangeListener;
import com.liwinner.mylive.params.AudioParams;
import com.liwinner.mylive.view.SrsCameraView;

/**
 * 作者：Hao on 2017/12/14 15:52
 * 邮箱：shengxuan@izjjf.cn
 */

public class BeautyFilterPush implements SrsCameraView.PreviewCallback, LiveStateChangeListener {
    private SrsCameraView mCameraView;
    private AudioPusher mAudioPusher;
    private PusherNative mPusherNative;
    private boolean isStartPush;
    public BeautyFilterPush(SrsCameraView srsCameraView,LiveStateChangeListener stateChangeListener) {
        this.mCameraView = srsCameraView;
        mCameraView.setPreviewCallback(this);
        mPusherNative = new PusherNative();
        mPusherNative.setLiveStateChangeListener(stateChangeListener);

        AudioParams audioParams = new AudioParams();
        mAudioPusher = new AudioPusher(mPusherNative,audioParams);
    }

    @Override
    public void onGetYuvFrame(byte[] data) {
        if (isStartPush) {
            mPusherNative.fireVideo(data);
        }
    }

    @Override
    public void onGetRgbaFrame(byte[] data, int width, int height) {
        if (isStartPush) {
            mPusherNative.fireVideo(data);
        }
    }

    public void setPreviewResolution(int width, int height) {
        mCameraView.setPreviewResolution(width, height);
        mPusherNative.setVideoOptions(width,height,480000,25);
    }

    public void startPreview() {
        mCameraView.startCamera();
    }

    public void startPush(String rtmpUrl) {
        isStartPush = true;
        mCameraView.startCamera();
        mAudioPusher.startPush();
        mPusherNative.startPush(rtmpUrl);
        mPusherNative.setLiveStateChangeListener(this);
    }

    public void stopPush() {
        isStartPush = false;
        mAudioPusher.stopPush();
        mPusherNative.stopPush();
        mPusherNative.removeLiveStateChangeListener();
    }

    @Override
    public void onError(int code) {

    }
}
