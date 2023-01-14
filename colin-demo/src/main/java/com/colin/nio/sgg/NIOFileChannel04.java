package com.colin.nio.sgg;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author: colin
 * @Create: 2023/1/14 19:10
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception {

        /**
         * Arsenal.jpeg → Arsenal_copy.jpeg
         */

        // 创建文件输入流对象
        FileInputStream fileInputStream = new FileInputStream("colin-demo/Arsenal.jpeg");
        // 获取输入流对象对应的 channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        // 创建文件输出流对象
        FileOutputStream fileOutputStream = new FileOutputStream("colin-demo/Arsenal_copy.jpeg");
        // 获取输出流对象对应的 chennel
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 使用 transferForm 完成文件拷贝
        outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());

        // 关闭资源
        inputStreamChannel.close();
        outputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
