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
    List<Type> types;

    private int typeIndex =0;

    private int byteIndex =0;

    private Type currentType;

    public ByteArray(List<Byte> bytes, List<Type> types) {
        this.bytes = bytes;
        this.types = types;
    }

    enum Type{

        SHORT(2),BOOLEAN(1),INT(4),BYTE(1);
        private final int length;

        Type(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }
    }

    public ByteArray() {
        bytes = new ArrayList<>();
        types = new ArrayList<>();
    }
    public void addShort(short num){
        byte[] numBytes = MathUtils.shortToByteArray(num);
        bytes.add(numBytes[0]);
        bytes.add(numBytes[1]);
        types.add(Type.SHORT);
    }
    public void addInt(int num){
        byte[] numBytes = MathUtils.intToByteArray(num);
        bytes.add(numBytes[0]);
        bytes.add(numBytes[1]);
        bytes.add(numBytes[2]);
        bytes.add(numBytes[3]);
        types.add(Type.INT);
    }
    public void addByte(byte num){
        bytes.add(num);
        types.add(Type.BYTE);
    }
    public void addBool(boolean num){
        bytes.add((byte)(num?1:0));
        types.add(Type.BOOLEAN);
    }

    synchronized private byte[] pop(){
        if(typeIndex<types.size() && byteIndex<bytes.size()){
            currentType = types.get(typeIndex);

            byte[] tmp = new byte[currentType.getLength()];
            switch (currentType) {
                case SHORT:
                    tmp[0] = bytes.get(byteIndex);
                    tmp[1] = bytes.get(byteIndex+1);
                    break;
                case INT:
                    tmp[0] = bytes.get(byteIndex);
                    tmp[1] = bytes.get(byteIndex+1);
                    tmp[2] = bytes.get(byteIndex+2);
                    tmp[3] = bytes.get(byteIndex+3);
                    break;
                case BYTE:
                case BOOLEAN:
                    tmp[0] = bytes.get(byteIndex);
                    break;
            }
            typeIndex++;
            byteIndex+=currentType.getLength();
            return tmp;
        }

        return null;
    }

    public void checkType(Type type) throws Exception {
        if(typeIndex<types.size()){
            if(types.get(typeIndex)!=type){
                throw new Exception("存入的源数据非"+type+",而是"+types.get(typeIndex));
            }
        }else {
            throw new Exception("数据已经清空完毕"+types.size());
        }

    }
    public int popInt() throws Exception {
        checkType(Type.INT);
        return MathUtils.byteArrayToInt(pop());
    }
    public short popShort() throws Exception {
        checkType(Type.SHORT);
        return (short)MathUtils.byteArrayToShort(pop());
    }
    public byte popByte() throws Exception {
        checkType(Type.BYTE);
        return pop()[0];
    }
    public boolean popBoolean() throws Exception {
        checkType(Type.BOOLEAN);
        return pop()[0]==1;
    }

    public Type getCurrentType() {
        return currentType;
    }


    public static void main(String[] args) throws Exception {
        ByteArray byteArray = new ByteArray();
        byteArray.addInt(10);
        byteArray.addShort((short) 30000);
        byteArray.addBool(true);
        byteArray.addByte((byte) 1);

        System.out.println(byteArray.popInt());
        System.out.println(byteArray.popShort());
        System.out.println(byteArray.popBoolean());
        System.out.println(byteArray.popByte());
    }
}
