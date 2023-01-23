package com.colin.netty.shang.inout;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/23 17:22
 */
@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        log.debug("服务器的 ip：{}，收到的消息为：{}",ctx.channel().localAddress(),msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("MyClientHandler 发送数据");
        // 一旦连接建立，便发送一个 Long
        ctx.writeAndFlush(123456L);
    }
}
