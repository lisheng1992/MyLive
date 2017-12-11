package com.liwinner.mylive.params;

/**
 * Created by liwinner on 2017/11/30.
 */

public class AudioParams {
    /**
     * 采样率，默认44100，这个采样率一般适配一切Android手机
     */
    private int sampleRateInHz = 44100;

    /**
     * 声道数，默认为单声道
     */
    private int channel = 1;

    public AudioParams(){}
    public AudioParams(int sampleRateInHz, int channel) {
        this.sampleRateInHz = sampleRateInHz;
        this.channel = channel;
    }

    public int getSampleRateInHz() {
        return sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
