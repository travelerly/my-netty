package com.colin.netty.shang.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: colin
 * @Create: 2023/1/22 16:29
 */
public class ColinServer {

    public static void main(String[] args) throws Exception {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在 bossGroup 增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 因为使用的是 http 协议，因此加入 http 的编码器和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 添加 ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                             * http 数据在传输过程中是分段的，HttpObjectAggregator 可以将多个段聚合起来
                             * 也就是浏览器发送大量数据时，会多次发出 http 请求的原因
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /*
                             * 对于 websocket，它的数据是以帧（frame）的形式传递的
                             * WebSocketFrame 有 6 个子类
                             * 浏览器请求时：ws://localhost:9000/hello   表示请求的 uri
                             * WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws(websocket) 协议，即保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义 handler，处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
