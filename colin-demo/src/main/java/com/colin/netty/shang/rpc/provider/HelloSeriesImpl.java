package com.colin.netty.shang.rpc.provider;

import com.colin.netty.shang.rpc.publicinterface.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: colin
 * @Create: 2023/1/26 14:36
 */
@Slf4j
public class HelloSeriesImpl implements HelloService {
    @Override
    public String hello(String msg) {
        log.debug("服务器收到客户端的消息是：{}",msg);
        // 根据 msg 返回不同结果
        if (msg != null){
            return "你好客户端，我已经收到你的消息：" + msg;
        } else {
            return "你好客户端，我已经收到你的消息：";
        }
    }
}
