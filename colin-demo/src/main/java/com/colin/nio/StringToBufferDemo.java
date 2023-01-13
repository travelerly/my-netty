package com.colin.nio;

import com.colin.util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author: colin
 * @Create: 2023/1/13 17:38
 */
public class StringToBufferDemo {

    public static void main(String[] args) {
        /**
         * 字符串与 ByteBuffer 互转
         */
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer buffer2 = Charset.forName("utf-8").encode("你好");

        ByteBufferUtil.debugAll(buffer1);
        ByteBufferUtil.debugAll(buffer2);

        CharBuffer buffer3 = StandardCharsets.UTF_8.decode(buffer2);
        System.out.println(buffer3.getClass());
        System.out.println(buffer3.toString());
    }
}
