package com.colin.netty.shang.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: colin
 * @Create: 2023/1/24 11:57
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 添加编码器
        ch.pipeline().addLast(new MyMessageEncoder());
        // 添加解码器
        ch.pipeline().addLast(new MyMessageDecoder());
        // 添加自定义处理器
        ch.pipeline().addLast(new MyClientHandler());
    }
}
