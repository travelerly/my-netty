package com.colin.netty.shang.inout;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: colin
 * @Create: 2023/1/23 17:04
 */
@Slf4j
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx 上下文
     * @param in 入站的 ByteBuf
     * @param out List 集合，将解码后的数据传给下一个 handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        log.debug("MyByteToLongDecoder 的 decode() 方法被调用，对数据进行解码");

        /**
         * long 为 8 个字节，每次读取都需要判断是否满足 8 个字节，满足才能进行读取
         * 每次读取 8 个字节加入到 out 集合中
         */
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
