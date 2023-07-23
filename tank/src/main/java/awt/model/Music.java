package awt.model;

import lombok.Data;

import javax.sound.sampled.AudioFormat;

@Data
public class Music {
    private AudioFormat audioFormat;// 文件格式
    byte tempBuffer[]; //默认全部读入缓存

    public Music(AudioFormat audioFormat, byte[] tempBuffer) {
        this.audioFormat = audioFormat;
        this.tempBuffer = tempBuffer;
    }
    public Music() {
    }
}
