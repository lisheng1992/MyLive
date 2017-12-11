package com.liwinner.mylive.params;

import android.hardware.Camera;

/**
 * Created by liwinner on 2017/11/30.
 */

public class VideoParams {
    /**
     * 码率 越大越清晰
     */
    private int bitrate = 480000;

    /**
     * 玩游戏的都知道，哈哈
     */
    private int fps = 30;

    private int width = 1280;
    private int height = 720;
    /**
     * 前、后摄像头
     */
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    public VideoParams(int width,int height,int cameraId) {
        this.cameraId = cameraId;
        this.width = width;
        this.height = height;
    }

    private VideoParams() {
    }

    public int getBitrate() {
        return bitrate;
    }

    public VideoParams setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public int getFps() {
        return fps;
    }

    public VideoParams setFps(int fps) {
        this.fps = fps;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public VideoParams setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public VideoParams setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getCameraId() {
        return cameraId;
    }

    public VideoParams setCameraId(int cameraId) {
        this.cameraId = cameraId;
        return this;
    }
}
