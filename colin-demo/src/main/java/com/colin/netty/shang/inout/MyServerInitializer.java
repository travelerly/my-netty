package com.colin.netty.shang.inout;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: colin
 * @Create: 2023/1/23 17:02
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        // 加入入站的解码器
        /*pipeline.addLast(new MyByteToLongDecoder());*/
        pipeline.addLast(new MyNewByteToLongDecoder());


        // 加入出站的编码器
        pipeline.addLast(new MyLongToByteEncoder());

        pipeline.addLast(new MyServerHandler());
    }
}
