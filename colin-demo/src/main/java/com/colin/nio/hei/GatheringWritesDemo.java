package com.colin.nio.hei;

import com.colin.util.ByteBufferUtil;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 聚集写入内容
 *
 * @author: colin
 * @Create: 2023/1/13 17:54
 */
public class GatheringWritesDemo {

    public static void main(String[] args) {

        try {
            // 3parts_W.txt 初始内容为：onetwothree
            RandomAccessFile file = new RandomAccessFile("colin-demo/3parts_W.txt", "rw");
            FileChannel channel = file.getChannel();

            /**
             * 准备多个 buffer，根据每个单词的字母个数创建对应的 buffer
             */
            ByteBuffer buffer1 = ByteBuffer.allocate(4);
            ByteBuffer buffer2 = ByteBuffer.allocate(4);

            // 设置 position，即设置写入位置
            channel.position(11);

            // 向缓冲区中写入数据
            buffer1.put(new byte[]{'f', 'o', 'u', 'r'});
            buffer2.put(new byte[]{'f', 'i', 'v', 'e'});

            // 切换 buffer 的模式
            buffer1.flip();
            buffer2.flip();

            ByteBufferUtil.debugAll(buffer1);
            ByteBufferUtil.debugAll(buffer2);

            // 向文件中写入数据
            channel.write(new ByteBuffer[]{buffer1,buffer2});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
