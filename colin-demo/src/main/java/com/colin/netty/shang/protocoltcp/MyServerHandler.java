package com.colin.netty.shang.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author: colin
 * @Create: 2023/1/24 12:04
 */
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 服务器接收数据，并处理数据
        int len = msg.getLen();
        byte[] content = msg.getContent();
        log.debug("服务器接收到的数据为：长度 = {}，内容 = {}",len,new String(content,Charset.forName("utf-8")));
        log.debug("服务器接收的消息包的数量为：{}",(++this.count));
        System.out.println("=======================================================================================");

        // 服务器向客户端回复消息
        byte[] responseContent = UUID.randomUUID().toString().getBytes("utf-8");
        int responseLength = responseContent.length;
        // 构建回显消息协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(responseContent);
        messageProtocol.setLen(responseLength);

        // 发送回显消息
        ctx.writeAndFlush(messageProtocol);
    }
}