package com.colin.nio;

import com.colin.util.ByteBufferUtil;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 分散读取文本文件
 *
 * @author: colin
 * @Create: 2023/1/13 17:41
 */
public class ScatteringReadsDemo {

    public static void main(String[] args) {
        try {
            // 3parts_R.txt 初始内容为 onetwothree
            RandomAccessFile file = new RandomAccessFile("colin-demo/3parts_R.txt", "rw");
            FileChannel channel = file.getChannel();

            /**
             * 准备多个 buffer，根据每个单词的字母个数创建对应的 buffer
             */
            ByteBuffer buffer1 = ByteBuffer.allocate(3);
            ByteBuffer buffer2 = ByteBuffer.allocate(3);
            ByteBuffer buffer3 = ByteBuffer.allocate(5);

            // 将数据填充至多个 buffer
            channel.read(new ByteBuffer[]{buffer1,buffer2,buffer3});

            // 切换 buffer 的读模式
            buffer1.flip();
            buffer2.flip();
            buffer3.flip();

            ByteBufferUtil.debugAll(buffer1);
            ByteBufferUtil.debugAll(buffer2);
            ByteBufferUtil.debugAll(buffer3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
