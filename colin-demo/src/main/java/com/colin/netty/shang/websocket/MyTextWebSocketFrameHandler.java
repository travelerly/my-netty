package com.colin.netty.shang.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 *
 * TextWebSocketFrame：表示一个文本帧（frame）
 * @author: colin
 * @Create: 2023/1/22 16:43
 */
@Slf4j
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.debug("服务器端收到消息 {}",msg.text());
        // 回复消息给浏览器
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间：" + LocalDate.now() + " " + msg.text()));
    }

    /**
     * 当 web 客户端连接后，就会触发此方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        /**
         * id 表示唯一的值，LongText 是唯一的值，而 ShortText 不是唯一的值
         */
        log.debug("handlerAdded 被调用了~ {}",ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.debug("handlerRemoved 被调用了~ {}",ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("异常发生 {}",cause.getMessage());
        // 关闭通道
        ctx.channel().close();
    }
}