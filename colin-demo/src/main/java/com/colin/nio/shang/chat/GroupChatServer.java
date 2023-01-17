package com.colin.nio.shang.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author: colin
 * @Create: 2023/1/16 18:13
 */
@Slf4j
public class GroupChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8081;

    // 构造器，完成初始化任务
    public GroupChatServer(){
        try {
            // 创建选择器
            selector = Selector.open();
            // 创建 ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置 ServerSocketChannel 为非阻塞模式
            listenChannel.configureBlocking(false);
            // ServerSocketChannel 注册到 Selector 上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
        	e.printStackTrace();
        }
    }

    // 监听相关逻辑
    public void listen(){
        try {
            while (true){
                int count = selector.select();
                if (count > 0){
                    // 说明有事件发生，需要处理。遍历 selectionKey 集合
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    while (selectionKeyIterator.hasNext()){
                        // 取出 selectionKey
                        SelectionKey selectionKey = selectionKeyIterator.next();

                        // 处理连接事件
                        if (selectionKey.isAcceptable()){
                            // 首先获取 SocketChannel
                            SocketChannel socketChannel = listenChannel.accept();
                            // 将该 SocketChannel 设置为非阻塞
                            socketChannel.configureBlocking(false);
                            // 将该 SocketChannel 注册到 Selector 上，关注 OP_READ 事件
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            // 给出提示
                            log.debug("{} 上线了~",socketChannel.getRemoteAddress());
                        }

                        // 处理 read 事件，即通道是可读状态
                        if (selectionKey.isReadable()){
                            readData(selectionKey);
                        }

                        // 删除当前的 selectionKey，防止重复处理
                        selectionKeyIterator.remove();
                    }

                } else {
                    // 说明当前没有事件发生
                    log.debug("等待 ……");
                }
            }
        }catch (Exception e){
        	e.printStackTrace();
        }finally {

        }
    }

    // 读取客户端消息
    private void readData(SelectionKey selectionKey){
        // 定义一个 socketChannel
        SocketChannel socketChannel = null;
        try {
            // 取到 selectionKey 关联的 socketChannel，即通过 selectionKey 反向获取 socketchannel
            socketChannel = (SocketChannel) selectionKey.channel();
            // 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            // 从 socketChannel 中将数据读取到 buffer 中
            int count = socketChannel.read(byteBuffer);
            // 根据读取到的大小 count 做处理
            if (count > 0){
                // 存在数据，将缓冲区中的数据转成字符串消息
                String msg = new String(byteBuffer.array());
                // 服务器输出该消息
                log.debug("form 客户端：{}",msg);

                // 并向其它客户端转发此消息
                sendInfoToOtherClients(msg,socketChannel);
            }
        } catch (IOException e){
        	try {
                // 离线提醒
                log.debug("{} 离线了",socketChannel.getRemoteAddress());
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                socketChannel.close();
        	}catch (IOException ioException){
                ioException.printStackTrace();
        	}
        }
    }

    // 向其它客户端转发消息(去掉自己)
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {
        // 先给出提示
        log.debug("服务器转发消息中 …… ");

        // 遍历所有注册到 Selector 上的 SocketChannel，并排除自己 self
        for (SelectionKey key : selector.keys()) {
            // 通过 key 反向获取其对应的 SocketChannel
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                // 将 msg 写入到 buffer 中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;
                // 将 buffer 的数据写入到通道中
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        // 创建服务器对象
        GroupChatServer server = new GroupChatServer();
        // 启动服务器
        server.listen();
    }
}
