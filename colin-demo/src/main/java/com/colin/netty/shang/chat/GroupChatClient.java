package com.colin.netty.shang.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author: colin
 * @Create: 2023/1/22 14:00
 */
@Slf4j
public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            ChannelFuture channelFuture = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new StringDecoder())
                                    .addLast("encode", new StringEncoder())
                                    .addLast(new GroupChatClientHanndler());
                        }
                    })
                    .connect(host, port)
                    .sync();
            Channel channel = channelFuture.channel();
            log.debug("客户端 {} 准备好了~",channel.localAddress());

            // 客户端输入内容
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                // 通过 channel 将 msg 发送到服务器
                channel.writeAndFlush(msg + "\r\n");
            }

        }finally {
            // 优雅关闭资源
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1",9000).run();
    }
}
