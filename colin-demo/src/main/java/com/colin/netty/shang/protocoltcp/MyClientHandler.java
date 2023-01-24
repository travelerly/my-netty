package com.colin.netty.shang.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author: colin
 * @Create: 2023/1/24 11:58
 */
@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端循环发送 5 条数据
        for (int i = 0; i < 5; i++) {
            String msg = "天气冷，吃火锅";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int len = msg.getBytes(Charset.forName("utf-8")).length;

            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(content);

            // 发送数据
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("异常消息为：{}",cause.getMessage());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 读取服务器的回显消息
        int len = msg.getLen();
        byte[] content = msg.getContent();
        log.debug("客户端接收到的消息如下：长度 = {}，内容 = {}",len,new String(content,Charset.forName("utf-8")));
        log.debug("客户端接收的消息包的数量为：{}",(++this.count));
        System.out.println("=======================================================================================");
    }
}
