

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class MusicTest implements Runnable{
    private String filePath;

    public MusicTest(String filePath){
        this.filePath = filePath;
    }

    @Override
    public void run() {

        AudioInputStream audioInputStream=null;// 文件流
        AudioFormat audioFormat;// 文件格式
        SourceDataLine sourceDataLine=null;// 输出设备
//        "/Users/chenbiao/tank/src/main/resources/tank.wav"
        File file = new File(filePath);


        // 取得文件输入流  

        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
            audioFormat = audioInputStream.getFormat();
            // 转换文件编码
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        audioFormat.getSampleRate(), 16, audioFormat.getChannels(),
                        audioFormat.getChannels() * 2, audioFormat.getSampleRate(),
                        false);
                audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
                        audioInputStream);
            }

            // 打开输出设备
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                    audioFormat, AudioSystem.NOT_SPECIFIED);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat); // 打开具有指定格式的行，这样可以使行获得所有所需的系统资源并变得可操作
            sourceDataLine.start();  // 允许某一数据行执行数据I/O
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }




        byte tempBuffer[] = new byte[320];
        try {
            int cnt;
            // 读取数据到缓存区 
            // 从音频流读取指定的最大数量的数据字节，并将其放入给定的字节数组中。
            // return: 读入缓冲区的总字节数；如果因为已经到达流末尾而不再有更多数据，则返回-1
            while ((cnt = audioInputStream.read(tempBuffer, 0,
                    tempBuffer.length)) != -1) {
                if (cnt > 0) {
                    // 写入缓存数据  
                    sourceDataLine.write(tempBuffer, 0, cnt); // 通过此源数据行将音频数据写入混频器
                }
            }
            // Block等待临时数据被输出为空  
            // 通过在清空数据行的内部缓冲区之前继续数据I/O，排空数据行中的列队数据
            sourceDataLine.drain();
            // 关闭行，指示可以释放的该行使用的所有系统资源。如果此操作成功，则将行标记为 closed，并给行的侦听器指派一个 CLOSE 事件。
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


}