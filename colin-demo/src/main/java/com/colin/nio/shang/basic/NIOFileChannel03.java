package com.colin.nio.shang.basic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: colin
 * @Create: 2023/1/14 19:10
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {

        /**
         * 1.txt → 2.txt
         */

        // 创建文件输入流对象
        FileInputStream fileInputStream = new FileInputStream("colin-demo/file/1.txt");
        // 获取输入流对象对应的 channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        // 创建文件输出流对象
        FileOutputStream fileOutputStream = new FileOutputStream("colin-demo/file/2.txt");
        // 获取输出流对象对应的 chennel
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 创建缓冲区对象
        ByteBuffer buffer = ByteBuffer.allocate(512);

        // 循环读取 1.txt 文件
        while (true){
            // 将 1.txt 的内容读取到缓冲区中，len 表示读取到的字节数，若 len 为 -1，则表示文件中没有未读取的数据了
            int len = inputStreamChannel.read(buffer);
            if (len == -1){
                // 文件内没有未读取的数据了，跳出循环
                break;
            }
            // 将缓冲区中的数据写入到输出流对应的通道中，即写入到 outputStreamChannel 中
            // 先切换缓冲区读写模式
            buffer.flip();
            outputStreamChannel.write(buffer);

            /**
             * 清空 buffer，方便下次循环使用
             * 防止 position = limit，从而造成 len = 0，而无法取得 -1，造成死循环
             */
            buffer.clear();
        }

        // 关闭资源
        inputStreamChannel.close();
        outputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
