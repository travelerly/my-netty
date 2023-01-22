package com.colin.netty.shang.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/22 14:00
 */
@Slf4j
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    // 编写 run 方法，处理客户端请求
    public void run() throws Exception {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 18)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new StringDecoder()) // 解码器
                                    .addLast("encoder", new StringEncoder()) // 编码器
                                    .addLast(new GroupChatServerHandler()); // 自定义处理器
                        }
                    })
                    .bind(port)
                    .sync();

            log.debug("netty 服务器启动");
            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();
        }finally {
            // 优雅关闭资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new GroupChatServer(9000).run();
    }
}
