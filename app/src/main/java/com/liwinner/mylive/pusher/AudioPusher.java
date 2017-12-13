package com.liwinner.mylive.pusher;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.liwinner.mylive.jni.PusherNative;
import com.liwinner.mylive.params.AudioParams;

/**
 * Created by liwinner on 2017/11/30.
 */

public class AudioPusher extends BasePusher {

    private AudioRecord mAudioRecord;

    private AudioParams mAudioParams;
    private PusherNative mPusherNative;
    /**
     * 缓冲区大小
     */
    private int minBufferSize;
    private boolean isRecording;
    private static final int AUDIO_RORMAT = AudioFormat.ENCODING_PCM_16BIT;
    public AudioPusher(PusherNative pusherNative, AudioParams audioParams) {
        this.mPusherNative = pusherNative;
        this.mAudioParams = audioParams;
        int channelConfig = audioParams.getChannel() == 1 ? AudioFormat.CHANNEL_IN_MONO:AudioFormat.CHANNEL_IN_STEREO;
        minBufferSize = AudioRecord.getMinBufferSize(mAudioParams.getSampleRateInHz(),channelConfig,AUDIO_RORMAT);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,mAudioParams.getSampleRateInHz(),
                channelConfig,AUDIO_RORMAT,minBufferSize);
    }

    @Override
    public void startPush() {
        isRecording = true;
        mPusherNative.setAudioOptions(mAudioParams.getSampleRateInHz(),mAudioParams.getChannel());
        new Thread(new AudioRecordRunnable()).start();
    }

    @Override
    public void stopPush() {
        isRecording = false;
        mAudioRecord.stop();
    }

    @Override
    public void release() {
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    class AudioRecordRunnable implements Runnable{
        @Override
        public void run() {
            mAudioRecord.startRecording();
            while (isRecording){
                byte[] buffer = new byte[minBufferSize];
                int len = mAudioRecord.read(buffer,0,minBufferSize);
                if (len > 0) {
                    //TODO 把PCM音频数据交给Native层编码
                    mPusherNative.fireAudio(buffer);
                }
            }
        }
    }
}
