package com.colin.netty.shang.simple;

import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/18 15:16
 */
@Slf4j
public class Test {

    public static void main(String[] args) {

        log.debug("CPU核数是：{}", NettyRuntime.availableProcessors());
    }
}
