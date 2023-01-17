package com.colin.netty.shang.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/17 20:00
 */
@Slf4j
public class NettyClientHander extends ChannelInboundHandlerAdapter {

    // 通道就绪时，就会触发此方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("ctx = {}",ctx);
        // 指定编码格式，将消息发送到通道中，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，服务端：ping", CharsetUtil.UTF_8));
    }

    // 读取事件，当通道中有读取事件时，会触发此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将消息转成 ByteBUf
        ByteBuf buf = (ByteBuf) msg;
        // 输出服务器返回的信息
        log.debug("服务器回复的消息是：{}",buf.toString(CharsetUtil.UTF_8));
        log.debug("服务器的地址是：{}",ctx.channel().remoteAddress());
    }

    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭通道
        ctx.channel().close();
    }
}
