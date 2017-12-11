package com.liwinner.mylive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @BindView(R.id.rtmp_url_et)
    AppCompatEditText rtmpUrlEt;
    @BindView(R.id.start_push_bt)
    Button startPushBt;

    private static final int PERMISSION_RECORD_AUDIO = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.start_push_bt)
    void startPush() {
        Intent intent = new Intent(StartActivity.this,MainActivity.class);
        intent.putExtra("RtmpUrl",rtmpUrlEt.getText().toString());
        startActivity(intent);
    }

    private void initView() {
        //申请录音与存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            //运行时申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    PERMISSION_RECORD_AUDIO);
        } else {
            //权限已申请
            Toast.makeText(this, "权限已申请", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限已申请", Toast.LENGTH_SHORT).show();
            } else {
                startPushBt.setClickable(false);
                Toast.makeText(this, "没有权限！", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
