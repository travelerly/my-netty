package com.colin.netty.shang.inout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: colin
 * @Create: 2023/1/23 18:46
 */
@Slf4j
public class MyNewByteToLongDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.debug("MyNewByteToLongDecoder 的 decode() 方法被调用，对数据进行解码");

        // ReplayingDecoder 不需要判断数据是否足够读写，其内部会进行处理判断
        out.add(in.readLong());
    }
}
