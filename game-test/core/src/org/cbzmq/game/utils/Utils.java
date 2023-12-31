package org.cbzmq.game.utils;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Utils {

    public static final int maxSize = 40960;

    public static class CompressData{
        byte[] output;
        int compressedDataLength;

        public CompressData(int size) {
            this.output = new byte[size];
        }

        public CompressData(byte[] output) {
            this.output = output;
        }

        public byte[] getOutput() {
            return output;
        }
    }


    /**
     * 压缩
     * @param data
     * @return
     */
    public static CompressData compress(byte[] data){
        // Compress the bytes
        Deflater compresser = new Deflater();
        CompressData compressData = new CompressData(data.length+100);

        compresser.setInput(data);
        compresser.finish();
        int compressedDataLength = compresser.deflate(compressData.output);
        compressData.compressedDataLength = compressedDataLength;

        byte[] result2 = new byte[compressedDataLength];

        System.arraycopy(compressData.output,0,result2,0,compressedDataLength);
//
//        for (int i = 0; i < compressedDataLength; i++) {
//            result2[i] = compressData.output[i];
//        }

        compresser.end();
//        System.out.println("压缩前数据量大小"+data.length+",压缩后数据量大小"+compressedDataLength);
        compressData.output = result2;
        return compressData;
    }

    /**
     * 解压缩
     * @param compressData
     * @return
     * @throws DataFormatException
     */
    public static byte[] decompress(byte[] compressData) throws DataFormatException {
        // Decompress the bytes
        Inflater decompresser = new Inflater();
        decompresser.setInput(compressData, 0, compressData.length);
        byte[] result = new byte[maxSize];
        int resultLength = decompresser.inflate(result);
        decompresser.end();
        byte[] result2 = new byte[resultLength];
        System.arraycopy(result,0,result2,0,resultLength);

//        System.out.println("压缩包数据量大小"+compressData.length+",解压后数据量大小"+resultLength);

        return result2;
    }

    public static void main(String[] args) throws DataFormatException {
        CompressData compress = compress("你是个大傻瓜大傻瓜大傻瓜大叔大叔大婶".getBytes());
        CompressData compress2 = compress("hahahaahahahahah".getBytes());
        byte[] decompress = decompress(compress.output);
        byte[] decompress2 = decompress(compress2.output);
        System.out.println(compress.output.toString());
        System.out.println(new String(decompress));

        System.out.println(compress2.output.toString());
        System.out.println(new String(decompress2));
    }

    public static byte[] shortToByteArray(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xff);
        }
        return shortBuf;
    }

    public static final int byteArrayToShort(byte[] b) {
        return (b[0] << 8)
                + (b[1] & 0xFF);
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    public static final int byteArrayToInt(byte[] b) {
        return (b[0] << 24)
                + ((b[1] & 0xFF) << 16)
                + ((b[2] & 0xFF) << 8)
                + (b[3] & 0xFF);
    }

    public static Counter createCounter(int initNum){
        return new Counter(initNum);
    }

    public static class Counter{
        int counter=0;
        private final int initNum;

        public Counter(int initNum) {
            this.initNum = initNum;
            this.counter = initNum;
        }
        synchronized public boolean reduce(){
            if(counter==0){
                return true;
            }else {
                counter--;
                return false;
            }
        }
        synchronized public void reset(){
            this.counter = initNum;
        }
    }


}
