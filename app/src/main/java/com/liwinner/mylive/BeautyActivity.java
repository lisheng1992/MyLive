package com.liwinner.mylive;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.liwinner.mylive.jni.PusherNative;
import com.liwinner.mylive.listener.LiveStateChangeListener;
import com.liwinner.mylive.pusher.BeautyFilterPush;
import com.liwinner.mylive.view.SrsCameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：Hao on 2017/12/14 16:59
 * 邮箱：shengxuan@izjjf.cn
 */

public class BeautyActivity extends AppCompatActivity implements LiveStateChangeListener {

    @BindView(R.id.glsurfaceview_camera)
    SrsCameraView mGlsurfaceviewCamera;
    @BindView(R.id.publish)
    Button mPublish;
    @BindView(R.id.swCam)
    Button mSwCam;
    @BindView(R.id.swFilter)
    Button mSwFilter;

    private BeautyFilterPush mFilterPush;
    private String rtmpUrl;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PusherNative.CONNECT_FAILED:
                    Toast.makeText(BeautyActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case PusherNative.INIT_FAILED:
                    Toast.makeText(BeautyActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty);
        ButterKnife.bind(this);
        rtmpUrl = getIntent().getStringExtra("RtmpUrl");
        mFilterPush = new BeautyFilterPush(mGlsurfaceviewCamera,this);
        mFilterPush.setPreviewResolution(1280,720);
        mFilterPush.startPreview();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
    }

    @OnClick(R.id.publish)
    public void startPush() {
        mFilterPush.startPush(rtmpUrl);
    }

    @Override
    public void onError(int code) {
        handler.sendEmptyMessage(code);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
    }
}
