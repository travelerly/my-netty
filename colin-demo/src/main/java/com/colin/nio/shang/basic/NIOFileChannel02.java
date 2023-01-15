package com.colin.nio.shang.basic;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: colin
 * @Create: 2023/1/14 16:24
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {

        /**
         * 读取 file01.txt 文件中的内容
         */

        // 创建一个输入流
        File file = new File("colin-demo/file/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过输入流获取对应的 FileChannel，实际类型是 FileChannelImpl
        FileChannel channel = fileInputStream.getChannel();
        // 创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

        // 将 channel 中的数据读取到缓冲区中
        channel.read(buffer);

        // 将缓冲区中的字节数据转成字符串并输出
        System.out.println(new String(buffer.array()));

    }
}