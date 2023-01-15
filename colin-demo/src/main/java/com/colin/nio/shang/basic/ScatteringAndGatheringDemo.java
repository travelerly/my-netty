package com.colin.nio.shang.basic;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author: colin
 * @Create: 2023/1/15 14:45
 */
public class ScatteringAndGatheringDemo {

    public static void main(String[] args) throws Exception{
        /**
         * Scattering(分散)：将数据写入到 buffer 时，可以采用 buffer 数组，依次写入
         * Gathering(聚合)：从 buffer 中读取数据时，可以采用 buffer 数组，依次读取
         */


        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8080);
        // 绑定端口到 socket，并启动
        serverSocketChannel.socket().bind(address);

        // 创建 buffer 数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(3);

        // 等待客户端链接
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 假定从客户端接收 8 个字节
        int msgLength = 8;

        while (true){
            // 表示读取到的字节数
            int byteRead = 0;
            while (byteRead < msgLength){
                long readLen = socketChannel.read(buffers);
                byteRead += readLen;
                // 累计读取的字节数
                System.out.println("byteRead" + byteRead);

                // 使用流打印，查看当前 buffer 的 position 和 limit
                Arrays.asList(buffers).stream().map(buffer ->
                    "position = " + buffer.position() + "，limit = " + buffer.limit()
                ).forEach(System.out::println);

            }

            // 将所有的 buffer 切换模式
            Arrays.asList(buffers).forEach(buffer -> buffer.flip());

            // 将数据写出显式到客户端
            long byteWrite = 0;
            while (byteWrite < msgLength){
                long writeLen = socketChannel.write(buffers);
                byteWrite += writeLen;
            }

            // 将所有的 buffer 清空
            Arrays.asList(buffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead = " + byteRead + "，byteWrite = " + byteWrite + "，msgLength = " + msgLength);

        }


    }
}
