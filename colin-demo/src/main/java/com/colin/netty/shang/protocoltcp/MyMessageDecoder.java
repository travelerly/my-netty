package com.colin.netty.shang.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: colin
 * @Create: 2023/1/24 12:37
 */
@Slf4j
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.debug("MyMessageDecoder 的 decode() 方法被调用，对消息进行解码");
        // 将得到的二进制字节码转成 MessageProtocol 数据包
        int length = in.readInt();
        byte[] contents = new byte[length];
        ByteBuf byteBuf = in.readBytes(contents);

        // 将数据封装成 MessageProtocol 对象，放入 out 中，传递至下一个 handler
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(contents);

        out.add(messageProtocol);
    }
}
