package com.colin.netty.shang.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/24 12:34
 */
@Slf4j
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        log.debug("MyMessageEncoder 的 encode() 方法被调用，对消息进行编码");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
