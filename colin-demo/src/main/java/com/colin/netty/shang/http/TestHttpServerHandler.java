package com.colin.netty.shang.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * 1.SimpleChannelInboundHandler：是 ChannelInboundHandlerAdapter 的子类，即也是一个处理器适配器
 * 2.HttpObject：表示客户端和服务器间相互通信的数据被封装成 HttpObject
 *
 * @author: colin
 * @Create: 2023/1/19 09:29
 */
@Slf4j
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // 读取客户端数据。当有读取事件发生时，会触发此方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断 msg 是否为 HttpRequest 请求
        if (msg instanceof HttpRequest) {

            log.debug("msg 的类型是：{}",msg.getClass());
            log.debug("客户端的地址是：{}",ctx.channel().remoteAddress());
            log.debug("pipiline hashCode：{}，handler hashCode ：{}",ctx.pipeline().hashCode(),this.hashCode());


            // msg 转换成 HttpRequest 对象
            HttpRequest httpRequest = (HttpRequest)msg;
            // 获取 URI
            URI uri = new URI(httpRequest.uri());
            // 过滤指定资源
            if ("/favicon.ico".equals(uri.getPath())) {
                log.debug("请求了资源：favicon.ico，此资源不做处理");
                return;
            }

            // 向浏览器回复信息（满足 http 协议）
            ByteBuf content = Unpooled.copiedBuffer("hello，我是服务器。", CharsetUtil.UTF_8);
            // 构造一个 http 响应对象，即 HttpResponse 对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            // 设置属性
            response.headers()
                    .set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
                    .set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的 response 返回
            ctx.writeAndFlush(response);
        }
    }
}
