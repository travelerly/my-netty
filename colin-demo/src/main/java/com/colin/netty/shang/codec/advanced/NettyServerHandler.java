package com.colin.netty.shang.codec.advanced;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class NettyServerHandler extends SimpleChannelInboundHandler<MyPOJO.MyMessage> {

    /**
     * 读取事件（读取客户端发送的消息）
     *
     * @param ctx 上下文对象，包含有管道（pipline）、通道（channel）、客户端地址等数据
     * @param msg 客户端发送的数据，默认是 Object 类型
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyPOJO.MyMessage msg) throws Exception {
        // 根据 dataType 来显式不同的信息
        MyPOJO.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyPOJO.MyMessage.DataType.UserType) {
            MyPOJO.User user = msg.getUser();
            log.debug("用户 id: {}，名称为：{}",user.getId(),user.getName());
        } else if (dataType == MyPOJO.MyMessage.DataType.WorkerType) {
            MyPOJO.Worker worker = msg.getWorker();
            log.debug("工人 name: {}，年龄为：{}",worker.getName(),worker.getAge());
        } else {
            log.debug("传输的类型错误❎");
        }
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
