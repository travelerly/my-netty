package com.colin.nio.shang.zerocopy;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author: colin
 * @Create: 2023/1/17 15:02
 */
@Slf4j
public class NewIOClient {

    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8081));
        String fileName = "colin-demo/file/protoc-3.6.1-win32.zip";
        // 获得一个文件 channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        // 准备发送
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        /**
         * 发送文件
         * transferTo()：底层采用的就是零拷贝
         */
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        log.debug("发送的总的字节数为 {}，总耗时为 {} 毫秒",transferCount,System.currentTimeMillis() - startTime);
    }
}