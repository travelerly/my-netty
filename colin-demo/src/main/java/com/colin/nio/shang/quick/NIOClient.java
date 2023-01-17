package com.colin.nio.shang.quick;


import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author: colin
 * @Create: 2023/1/16 14:24
 */
@Slf4j
public class NIOClient {

    public static void main(String[] args) throws Exception {

        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 将 SocketChannel 设置为非阻塞模式
        socketChannel.configureBlocking(false);
        // 设置服务器端 ip 和端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);

        // 链接服务器
        if (!socketChannel.connect(address)){
            while (!socketChannel.finishConnect()){
                log.debug("因为链接需要时间，客户端不会阻塞，可以做其它工作 ……");
            }
        }

        // 链接成功，发送数据
        String str = "hello colin";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 将 buffer 中的数据写入到 channel 中
        socketChannel.write(buffer);

        System.in.read();
    }
}
