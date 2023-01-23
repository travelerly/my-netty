package com.colin.netty.shang.codec.advanced;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/17 19:49
 */
@Slf4j
public class NettyClient {

    public static void main(String[] args) throws Exception {

        // 创建一个事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            // 创建客户端的启动对象（客户端使用的是 Bootstrap），并设置相关参数
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 向 pipline 中加入 ProtobufEncoder 编码器
                            ch.pipeline().addLast("encoder",new ProtobufEncoder());
                            ch.pipeline().addLast(new NettyClientHander());
                        }
                    });

            // 给出提示
            log.debug("客户端已就绪");

            // 启动客户端，连接服务器。（ChannelFuture 涉及到 netty 的异步模型）
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            // 优雅关闭资源
            eventLoopGroup.shutdownGracefully();
        }
    }
}
