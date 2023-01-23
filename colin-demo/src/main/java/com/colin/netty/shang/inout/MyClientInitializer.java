package com.colin.netty.shang.inout;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: colin
 * @Create: 2023/1/23 17:16
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 出站之前加入编码器（出站 handler，对数据进行编码）
        pipeline.addLast(new MyLongToByteEncoder());
        // 再加入一个自定义的 handler，处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
