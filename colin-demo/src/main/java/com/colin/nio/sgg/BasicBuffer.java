package com.colin.nio.sgg;

import java.nio.IntBuffer;

/**
 * @author: colin
 * @Create: 2023/1/14 15:03
 */
public class BasicBuffer {

    public static void main(String[] args) {
        // 举例说明 buffer 的使用

        /**
         * 创建一个 buffer，大小为 5，即可以存放 5 个 int 值
         * Buffer 种类有：IntBuffer、FloatBuffer、CharBUffer、DoubleBuffer、ShortBuffer、LongBuffer、ByteBuffer
         * 可以根据数据类型选择对应的 Buffer
         */
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向 buffer 中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 从 buffer 中读取数据
        // buffer 切换到读模式
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            // get()：每次执行，索引指针都会向后移动一位
            System.out.println(intBuffer.get());
        }
    }
}
