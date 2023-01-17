package com.colin.netty.shang.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 的自定义处理器（Handler），需要继承符合 netty 规范的处理器适配器（HandlerAdapter）
 * 即需要继承 netty 规定的某个处理器适配器
 *
 * @author: colin
 * @Create: 2023/1/17 19:28
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取事件（读取客户端发送的消息）
     *
     * @param ctx 上下文对象，包含有管道（pipline）、通道（channel）、客户端地址等数据
     * @param msg 客户端发送的数据，默认是 Object 类型
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("server ctx = {}",ctx);
        // 将 msg 转成 ByteBuf（netty 提供的）
        ByteBuf buf = (ByteBuf) msg;
        log.debug("客户端发送的数据是：{}",buf.toString(CharsetUtil.UTF_8));
        log.debug("客户端地址是：{}",ctx.channel().remoteAddress());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 指定编码格式，将服务器返回的信息发送到通道中，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：pong",CharsetUtil.UTF_8));
    }

    // 处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭通道
        ctx.channel().close();
    }
}
