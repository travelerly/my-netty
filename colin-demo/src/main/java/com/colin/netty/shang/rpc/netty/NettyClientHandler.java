package com.colin.netty.shang.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * @author: colin
 * @Create: 2023/1/26 14:59
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    // 上下文
    private ChannelHandlerContext context;
    // 将来返回的结果
    private String result;
    // 客户端调用方法时，传入的参数
    private String para;


    /**
     * 客户端与服务器连接建立后，就会调用此方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 其它方法需要使用到上下文
        context = ctx;
    }

    /**
     * 客户端收到服务器数据后，就会调用此方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("客户端开始接收服务器的消息~");
        result = msg.toString();
        // 唤醒等待的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    // 被代理对象调用，发送数据给服务器，发送完成后，就会调用 wait() 方法等待被唤醒（被 channelRead() 方法里的 notify() 唤醒）
    @Override
    public synchronized Object call() throws Exception {
        // 给服务器发送消息
        log.debug("客户端给服务器发送消息：{}",para);
        context.writeAndFlush(para);
        // 进行 wait 阻塞，即等待被唤醒
        wait();
        // 等待 channelRead() 方法获取到服务器返回的结果后唤醒，然后返回结果
        log.debug("客户端接收到服务器的消息：{}",para);
        return result;
    }

    void setPara(String para){
        this.para = para;
    }

}
