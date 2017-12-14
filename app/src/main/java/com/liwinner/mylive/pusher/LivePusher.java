package com.liwinner.mylive.pusher;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.liwinner.mylive.jni.PusherNative;
import com.liwinner.mylive.listener.LiveStateChangeListener;
import com.liwinner.mylive.params.AudioParams;
import com.liwinner.mylive.params.VideoParams;

/**
 * Created by liwinner on 2017/12/11.
 */

public class LivePusher implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private VideoPusher mVideoPusher;
    private AudioPusher mAudioPusher;
    private PusherNative mPusherNative;
    private Context mContext;

    public LivePusher(SurfaceHolder surfaceHolder,Context context){
        mSurfaceHolder = surfaceHolder;
        mSurfaceHolder.addCallback(this);
        mContext = context;
        prepare();
    }

    private void prepare() {
        mPusherNative = new PusherNative();
        AudioParams audioParams = new AudioParams();
        mAudioPusher = new AudioPusher(mPusherNative,audioParams);

        VideoParams videoParams = new VideoParams(1280,720, Camera.CameraInfo.CAMERA_FACING_BACK);
        mVideoPusher = new VideoPusher(mContext,mSurfaceHolder,videoParams,mPusherNative);
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        mVideoPusher.switchCamera();
    }

    /**
     * 开始推流
     * @param url
     * @param liveStateChangeListener
     */
    public void startPush(String url,LiveStateChangeListener liveStateChangeListener) {
        mVideoPusher.startPush();
        mAudioPusher.startPush();
        mPusherNative.startPush(url);
        mPusherNative.setLiveStateChangeListener(liveStateChangeListener);
    }


    /**
     * 停止推流
     */
    public void stopPush() {
        mVideoPusher.stopPush();
        mAudioPusher.stopPush();
        mPusherNative.stopPush();
        mPusherNative.removeLiveStateChangeListener();
    }

    /**
     * 释放资源
     */
    private void release() {
        mVideoPusher.release();
        mAudioPusher.release();
        mPusherNative.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPush();
        release();
    }
}
