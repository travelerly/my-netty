package com.colin.netty.shang.codec.simple;

import io.netty.buffer.ByteBuf;
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
        // 发送一个 Student 对象到服务器
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(1024).setName("colin").build();
        ctx.writeAndFlush(student);
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
