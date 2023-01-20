package com.colin.netty.shang.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author: colin
 * @Create: 2023/1/19 17:16
 */
@Slf4j
public class NettyByteBuf02 {

    public static void main(String[] args) {

        /**
         * 创建一个 ByteBuf
         */
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);

        // 使用相关 API
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            log.debug("content 的内容是：{}",new String(content, Charset.forName("utf-8")));
            log.debug("ByteBuf 的类型是：{}",byteBuf);
        }

        // 循环取出各个字节
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println((char)byteBuf.getByte(i));
        }

        log.debug("读取指定范围的内容是：{}",byteBuf.getCharSequence(0,4,Charset.forName("utf-8")));
        log.debug("读取指定范围的内容是：{}",byteBuf.getCharSequence(4,6,Charset.forName("utf-8")));
    }
}
