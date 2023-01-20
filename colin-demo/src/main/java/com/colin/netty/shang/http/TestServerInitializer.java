package com.colin.netty.shang.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/19 09:30
 */
@Slf4j
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 向管道加入处理器，
        /**
         * 1.先向管道添加一个 netty 提供的 HttpServerCodec 编解码器
         * HttpServerCodec 是 netty 提供的处理 http 的编解码器
         */
        socketChannel.pipeline().addLast("MyHttpServerCodec",new HttpServerCodec());
        // 2.向管道添加一个自定义的 handler
        socketChannel.pipeline().addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

        log.debug("ok ~~~");
    }
}
