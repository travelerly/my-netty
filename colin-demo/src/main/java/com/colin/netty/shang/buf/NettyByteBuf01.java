package com.colin.netty.shang.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author: colin
 * @Create: 2023/1/19 17:08
 */
public class NettyByteBuf01 {

    public static void main(String[] args) {
        /**
         * 创建一个 ByteBuf
         * 1.创建一个 ByteBuf 对象，该对象包含一个数组 arr，arr 是一个 byte[10] 的数组
         * 2.在 netty 的 ByteBuf 中，不需要使用 flip() 方法进行反转（读写模式切换），
         *      因为底层维护了一个 readerIndex（下一个读取的位置） 和 writerIndex（下一个写入的位置）
         * 3.通过 readerIndex、writerIndex 和 capacity，将 ByteBuf 分成三个区域
         *      0 ~ readerIndex：表示已经读取过的区域
         *      readerIndex ~ writerIndex：表示可读区域
         *      writerIndex ~ capacity：表示可写的区域
         */
        ByteBuf buf = Unpooled.buffer(10);


        // 循环赋值
        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        // 输出
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println(buf.readByte());
        }
    }
}
