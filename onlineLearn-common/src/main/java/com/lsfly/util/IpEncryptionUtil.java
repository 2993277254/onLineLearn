package com.lsfly.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IpEncryptionUtil {

    /**
     * Int型转byte型
     * @param param
     * @return
     */
    public static byte intToByte(int param){
        return (byte)param;
    }

    /**
     * 加密摄像头ip地址
     *
     * @param ip 摄像头地址
     * @return 加密后摄像头地址
     * @author christinRuan
     * @date 2017/4/7  9:42
     * @returnType int
     */
    public static int convertNetBytesToHostBytes(String ip) {
        String[] StringArr = ip.split("\\.");
        byte[] bytes = {intToByte(Integer.valueOf(StringArr[0])),
                intToByte(Integer.valueOf(StringArr[1])),
                intToByte(Integer.valueOf(StringArr[2])),
                intToByte(Integer.valueOf(StringArr[3]))};
        ByteBuffer buffer2 = ByteBuffer.wrap(bytes);
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        return buffer2.getInt();
    }
    public static void main(String[] args){
        System.out.println(convertNetBytesToHostBytes("192.168.1.168"));
    }
}
