package com.colin.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: colin
 * @Create: 2023/1/13 15:56
 */
@Slf4j
public class ChannelDemo {

    public static void main(String[] args) {
        try(FileChannel channel = new FileInputStream("colin-demo/data.txt").getChannel()) {
            // 初始化一个字节缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);

            do {
                // 向 buffer 写入数据
                int len = channel.read(buffer);
                log.debug("本次写入到 buffer 内的字节数是 {}",len);
                if (len == -1){
                    break;
                }

                // 切换 buffer 读模式
                buffer.flip();

                // 校验是否还有剩余未读的数据
                while (buffer.hasRemaining()){
                    byte data = buffer.get();
                    log.debug("读取到的内容是 {}",(char)data);
                }

                // 切换 buffer 的写模式
                buffer.clear();
            }while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
