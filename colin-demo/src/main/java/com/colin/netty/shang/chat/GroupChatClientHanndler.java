package com.colin.netty.shang.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/22 14:54
 */
@Slf4j
public class GroupChatClientHanndler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg.trim());
        log.debug(msg);
    }
}
