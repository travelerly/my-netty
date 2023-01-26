package com.colin.netty.shang.rpc.netty;

import com.colin.netty.shang.rpc.provider.HelloSeriesImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/26 14:48
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("服务端与客户端连接建立");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息，并调用目标服务
        log.debug("获取客户端发送的消息 msg = {}",msg);

        /**
         * 客户端在调用服务器的 api 时，需要定义一个协议
         * 例如：每次发消息时，都必须以某个字符串开头 "HelloService#hello#xxx"
         */
        if (msg.toString().startsWith("HelloService#hello#")){
            // 截取请求 uri
            String uri = msg.toString().substring(msg.toString().lastIndexOf("#") + 1);
            String result = new HelloSeriesImpl().hello(uri);
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
