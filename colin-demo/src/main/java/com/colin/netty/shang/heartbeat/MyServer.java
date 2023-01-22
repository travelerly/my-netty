package com.colin.netty.shang.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author: colin
 * @Create: 2023/1/22 15:44
 */
public class MyServer {

    public static void main(String[] args) throws Exception {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在 bossGroup 增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * 添加一个 netty 提供的 IdleStateHandler
                             * IdleStateHandler：是处理空闲状态的处理器
                             * long readerIdleTime：表示多长时间没有读操作，就会发送一个心跳检测包，检测是否还是连接状态
                             * long writerIdleTime：表示多长时间没有写操作，就会发送一个心跳检测包，检测是否还是连接状态
                             * long allIdleTime：表示多长时间没有读写操作，就会发送一个心跳检测包，检测是否还是连接状态
                             * TimeUnit unit：时间单位
                             *
                             * IdleStateHandler：
                             * Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                             * read, write, or both operation for a while.
                             *
                             * 当 IdleStateEvent 事件触发后，会传递给管道的下一个 handler 处理，
                             * 通过调用下一个 handler 的 userEventTriggered 方法去处理 IdleStateEvent 事件（读空闲、写空闲、读写空闲）
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的自定义 handler
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(9000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
