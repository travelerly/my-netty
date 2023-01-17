package com.colin.nio.shang.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author: colin
 * @Create: 2023/1/16 19:02
 */
@Slf4j
public class GroupChatClient {

    // 定义相关属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 8081;
    private Selector selector = null;
    private SocketChannel socketChannel;
    private String username;

    // 构造器，完成初始化
    public GroupChatClient() throws IOException {
        // 获取到 Selector
        selector = Selector.open();
        // 连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        // 设置 socketChannel 为非阻塞模式
        socketChannel.configureBlocking(false);
        // 将 socketChannel 注册到 Selector 上，关注 OP_READ 事件
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 使用 ip 做为 username
        username = socketChannel.getLocalAddress().toString().substring(1);
        // 客户端给出提示
        log.debug("客户端 {} 准备好了~",username);
    }

    // 向服务器发送消息
    public void sentInfo(String info){
        info = username + "说：" + info;
        try {
            // 将消息发送到通道中
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception e){
        	e.printStackTrace();
        }
    }

    // 从服务器端读取回复的消息
    public void readInfo(){
        try {
            int readChannels = selector.select();
            if (readChannels > 0){
                // 说明有事件发生，需要处理。遍历 selectionKey 集合
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()){
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    // 处理 read 事件，即可读事件
                    if (selectionKey.isReadable()){
                        // 根据 selectionKey 反向获取对应的 socketChannel
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        // 创建一个 buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 将通道中的数据读取到 buffer 中
                        int read = socketChannel.read(buffer);
                        String msg = new String(buffer.array());
                        log.debug(msg.trim());
                    }
                    // 删除当前的 selectionKey，防止重复处理
                    selectionKeyIterator.remove();
                }
            } else {

            }
        }catch (Exception e){
        	e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        // 启动一个线程，每隔 3 秒，读取服务器端发送的数据
        new Thread(()->{
            while (true){
                // 调用读取数据的方法
                chatClient.readInfo();
                // 休眠 3 秒
                try {
                    Thread.currentThread().sleep(3000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            // 调用发送消息方法
            chatClient.sentInfo(msg);
        }
    }
}
