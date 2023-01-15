package com.colin.nio.shang.basic;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: colin
 * @Create: 2023/1/15 14:17
 */
public class MappedByteBufferDemo {

    /**
     * MappedByteBuffer：可以让文件直接在内存（对外内存）中修改，操作系统无需进行拷贝操作
     */
    public static void main(String[] args) throws Exception {

        // mapped.txt 文件中起始内容为 1234567890
        RandomAccessFile file = new RandomAccessFile("colin-demo/file/mapped.txt", "rw");
        // 获取对应的通道
        FileChannel channel = file.getChannel();

        /**
         * 参数（MapMode mode,long position, long size）
         * MapMode：表示读写模式
         * position：表示（直接）修改的起始位置
         * size：表示映射到内存的大小（不是索引位置），即将 1.txt 的多少个字节映射到内存
         * 综上，即可以修改的范围是 [0~5) 个字节
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte)'A');
        mappedByteBuffer.put(4,(byte)'B');

        // 修改位置超出了允许修改的字节大小，抛出异常 IndexOutOfBoundsException
        /*mappedByteBuffer.put(5,(byte)'Y');*/

        // 关闭资源
        file.close();
        channel.close();
    }
}
