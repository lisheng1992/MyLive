package com.liwinner.mylive.pusher;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.liwinner.mylive.jni.PusherNative;
import com.liwinner.mylive.params.VideoParams;

import java.io.IOException;

/**
 * Created by liwinner on 2017/11/30.
 */

public class VideoPusher extends BasePusher implements SurfaceHolder.Callback ,Camera.PreviewCallback{

    private SurfaceHolder mSurfaceHolder;
    private VideoParams mVideoParams;
    private Camera mCamera;
    private boolean isPushing = false;
    private int iDegrees;
    private byte[] buffers;
    private Context mContext;
    private PusherNative mPusherNative;
    public VideoPusher(Context context,SurfaceHolder surfaceHolder, VideoParams videoParams, PusherNative pusherNative) {
        this.mSurfaceHolder = surfaceHolder;
        this.mVideoParams = videoParams;
        this.mContext = context;
        this.mPusherNative = pusherNative;
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void startPush() {
        mPusherNative.setVideoOptions(mVideoParams.getWidth(),mVideoParams.getHeight(),mVideoParams.getBitrate(),mVideoParams.getFps());
        isPushing = true;
    }

    @Override
    public void stopPush() {
        isPushing = false;
    }

    @Override
    public void release() {
        stopPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    startPreview();
                    camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦
                }
            }
        });
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        if(mCamera != null){
            mCamera.addCallbackBuffer(buffers);
        }
        if(isPushing){
            //回调函数中获取图像数据，然后给Native代码编码
            mPusherNative.fireVideo(bytes);
        }
    }
    /**
     * 初始化相机，开始预览
     */
    private void startPreview() {
        try{
            //SurfaceView初始化完成，开始相机预览
            iDegrees = getDisplayOritation(getDispalyRotation(),mVideoParams.getCameraId());
            if (mCamera == null) {
                mCamera = Camera.open(mVideoParams.getCameraId());
            }
            Camera.Parameters parameters = mCamera.getParameters();
            //设置相机参数
            parameters.setPreviewFormat(ImageFormat.NV21); //YUV 预览图像的像素格式
            parameters.setPreviewSize(mVideoParams.getHeight(), mVideoParams.getWidth()); //预览画面宽高
            parameters.setPreviewFrameRate(mVideoParams.getFps());
            mCamera.setDisplayOrientation(iDegrees);
            parameters.setRotation(iDegrees);
            mCamera.setParameters(parameters);
            //获取预览图像数据
            if (buffers == null) {
                buffers = new byte[mVideoParams.getWidth() * mVideoParams.getHeight() * 4];
            }
            mCamera.addCallbackBuffer(buffers);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //只有加上了这一句，才会自动对焦。
            mCamera.cancelAutoFocus();
            mCamera.startPreview();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void switchCamera() {
        if(mVideoParams.getCameraId() == Camera.CameraInfo.CAMERA_FACING_BACK){
            mVideoParams.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }else{
            mVideoParams.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        //重新预览
        stopPreview();
        startPreview();
    }

    private int getDispalyRotation() {
        int i = ((AppCompatActivity)mContext).getWindowManager().getDefaultDisplay().getRotation();
        switch (i) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    private int getDisplayOritation(int degrees, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}
