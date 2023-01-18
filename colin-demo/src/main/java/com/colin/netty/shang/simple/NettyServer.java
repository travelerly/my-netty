package com.colin.netty.shang.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/17 19:06
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) throws Exception {

        /**
         * 创建 BossGroup 和 WorkerGroup
         * 1.创建两个线程组：bossGroup 和 workerGroup
         * 2.bossGroup 只处理连接请求，workerGroup 处理客户端的业务请求
         * 3.两个线程组内部都是无限循环
         * 4.bossGroup 和 workerGroup 所包含的子线程（EventLoop）数量是可以指定的，默认的数量是 CPU 核数的两倍
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，并配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象（匿名对象方式）
                        // 给 pipline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });// 给 workerGroup 中 EventLoop 对应的管道设置处理器

            // 给出提示
            log.debug("服务器已就绪");

            // 启动服务器（绑定端口并同步），生成一个 ChannelFuture 对象
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
            // 为 ChannelFuture 添加监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()){
                        log.debug("监听 6668 端口成功");
                    } else {
                        log.debug("监听 6668 端口失败");
                    }
                }
            });

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            // 优雅关闭资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
