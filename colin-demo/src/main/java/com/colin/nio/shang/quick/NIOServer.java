package com.colin.nio.shang.quick;


import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: colin
 * @Create: 2023/1/15 19:38
 */
@Slf4j
public class NIOServer {

    public static void main(String[] args) throws Exception {

        // 创建 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个 Selector 对象
        Selector selector = Selector.open();

        // 绑定端口，ServerSocketChannel 监听
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        // 设置 ServerSocketChannel 为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 把 ServerSocketChannel 注册到 Selector，关心的事件类型为 OP_ACCEPT
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端链接
        while (true){

            // 等待 1s，若没有事件发生，则继续下一次循环
            if (selector.select(1000) == 0){
                // 说明此时没有事件发生，
                log.debug("服务器等待了 1s，无连接");
                continue;
            }

            // 若有事件发生，则获取所关注的事件的所有的 selectionKey 的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历集合，通过 selectionKey 来获取对应的 channel
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取 selectionKey
                SelectionKey key = keyIterator.next();

                // 不同事件，不同处理逻辑
                // 发生了 OP_ACCEPT 事件
                if (key.isAcceptable()) {
                    // 获取对应的 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    log.debug("客户端链接成功，生成了一个 SocketChannel：{}",socketChannel.hashCode());
                    // 将 SocketChannel 设置为非阻塞模式
                    socketChannel.configureBlocking(false);

                    // 将 socketChannel 也注册到 Selector 上，关心的事件是 OP_READ，同时给 socketChannel 关联一个 buffer
                    SelectionKey channelSelectionKey = socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(15));
                }

                // 发生了 OP_READ 事件
                if (key.isReadable()){
                    // 通过 key 反向获取 socketchannel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 通过 key 反向获取到 SocketChannel 关联的 buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    // 将 channel 中的数据读取到 buffer 中
                    int len = channel.read(buffer);
                    log.debug("from 客户端：{}",new String(buffer.array()));
                }

                // 手动删除 SelectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
