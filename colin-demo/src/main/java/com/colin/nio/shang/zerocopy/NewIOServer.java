package com.colin.nio.shang.zerocopy;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author: colin
 * @Create: 2023/1/17 09:36
 */
public class NewIOServer {

    public static void main(String[] args) throws Exception {

        // 创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8081));
        // 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        // 循环监听
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while (readCount != -1){
                try {
                    readCount = socketChannel.read(byteBuffer);
                }catch (Exception e){
                	break;
                }
                // 倒带：position = 0，mark 作废
                byteBuffer.rewind();
            }
        }
    }
}