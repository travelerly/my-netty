package com.colin.netty.shang.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

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
        /*log.debug("服务器读取线程：{}",Thread.currentThread().getName());
        log.debug("server ctx = {}",ctx);

        *//**
         * 了解 channel 和 pipline
         * pipline：本质是一个双向链表，设计出栈和入栈
         *//*
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();

        // 将 msg 转成 ByteBuf（netty 提供的）
        ByteBuf buf = (ByteBuf) msg;
        log.debug("客户端发送的数据是：{}",buf.toString(CharsetUtil.UTF_8));
        log.debug("客户端地址是：{}",ctx.channel().remoteAddress());*/

        /**
         * 模拟一个耗时的业务操作，需要异步执行，
         * 将任务提交到该 channel（通道）对应的 NIOEventLoop 的 taskQueue 队列中
         */
        // 方式一：自定义普通任务，将任务提交至 NIOEventLoop 的 taskQueue 队列中
        ctx.channel().eventLoop().execute(()->{
            // 任务一
            try {
                // 耗时任务，向客户端返回一段文字
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：我是模拟的自定义普通任务一。",CharsetUtil.UTF_8));
            }catch (Exception e){
                log.debug("发生了以下异常：{}",e.getMessage());
            }
        });

        ctx.channel().eventLoop().execute(()->{
            // 任务二（任务二的执行是在任务一执行完毕后，才开始）
            try {
                // 耗时任务，向客户端返回一段文字
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：我是模拟的自定义普通任务二。",CharsetUtil.UTF_8));
            }catch (Exception e){
                log.debug("发生了以下异常：{}",e.getMessage());
            }
        });

        // 方式二：自定义定时任务，该任务提交到 scheduleTaskQueue 队列中
        ctx.channel().eventLoop().schedule(()->{
            // 定时任务，延迟 5 秒执行
            try {
                // 耗时任务，向客户端返回一段文字
                Thread.sleep(8000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端：我是模拟的自定义定时任务。",CharsetUtil.UTF_8));
            }catch (Exception e){
                log.debug("发生了以下异常：{}",e.getMessage());
            }
        },5, TimeUnit.SECONDS);



        log.debug("go on ……");

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
