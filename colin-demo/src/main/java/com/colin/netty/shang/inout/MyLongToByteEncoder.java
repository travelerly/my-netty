package com.colin.netty.shang.inout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/23 17:19
 */
@Slf4j
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        log.debug("MyLongToByteEncoder 中的 encode() 方法被调用，对数据进行编码，数据为：{}",msg);
        out.writeLong(msg);
    }
}
