package org.cbzmq.game.proto;

import org.cbzmq.game.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ByteArray
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/4 1:52 下午
 * @Version 1.0
 **/
public class ByteArray {
    List<Byte> bytes;
    List<Integer> index;

    public ByteArray() {
        bytes = new ArrayList<>();
        index= new ArrayList<>();
    }
    public void addShort(short num){
        byte[] numBytes = MathUtils.shortToByteArray(num);
        bytes.add(numBytes[0]);
        bytes.add(numBytes[1]);
        index.add(2);
    }
    public void addInt(int num){
        byte[] numBytes = MathUtils.intToByteArray(num);
        bytes.add(numBytes[0]);
        bytes.add(numBytes[1]);
        bytes.add(numBytes[2]);
        bytes.add(numBytes[3]);
        index.add(4);
    }
    public void addByte(byte num){
        bytes.add(num);
        index.add(1);
    }
    public void addBool(){

    }
}
