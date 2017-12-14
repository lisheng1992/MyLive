package com.liwinner.mylive;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.liwinner.mylive.jni.PusherNative;
import com.liwinner.mylive.listener.LiveStateChangeListener;
import com.liwinner.mylive.pusher.LivePusher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LiveStateChangeListener{

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.change_cameraid_bt)
    Button changeCameraidBt;
    @BindView(R.id.start_push_bt)
    Button mStartPushBt;

    private LivePusher mLivePusher;
    private PowerManager.WakeLock mWakeLock;
    private String rtmpUrl;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PusherNative.CONNECT_FAILED:
                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case PusherNative.INIT_FAILED:
                    Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLivePusher = new LivePusher(surfaceView.getHolder(),this);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        rtmpUrl = getIntent().getStringExtra("RtmpUrl");
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

    @OnClick(R.id.start_push_bt)
    public void startPush() {
        if (mStartPushBt.getText().equals("开始")) {
            mLivePusher.startPush(rtmpUrl,this);
            mStartPushBt.setText("停止");
        } else if(mStartPushBt.getText().equals("停止")) {
            mLivePusher.stopPush();
            mStartPushBt.setText("开始");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onError(int code) {
        handler.sendEmptyMessage(code);
    }
}
