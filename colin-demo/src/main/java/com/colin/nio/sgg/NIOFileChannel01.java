package com.colin.nio.sgg;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: colin
 * @Create: 2023/1/14 16:24
 */
public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {

        String str = "hello colin";

        // 创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("colin-demo/file01.txt");
        // 通过输出流获取对应的 FileChannel，其真是类型是 FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 将数据 str 放入到缓冲区中
        buffer.put(str.getBytes());
        // buffer 反转，切换读写模式
        buffer.flip();

        // 将缓冲区中的数据写入到 channel 中
        channel.write(buffer);

        // 关闭流
        fileOutputStream.close();
    }
}