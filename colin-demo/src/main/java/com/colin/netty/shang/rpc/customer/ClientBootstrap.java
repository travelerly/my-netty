package com.colin.netty.shang.rpc.customer;

import com.colin.netty.shang.rpc.netty.NettyClient;
import com.colin.netty.shang.rpc.publicinterface.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * ClientBootstrap 会启动一个服务消费者，其实就是 Netty Client
 * @author: colin
 * @Create: 2023/1/26 15:36
 */
@Slf4j
public class ClientBootstrap {

    // 定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) {

        // 创建一个消费者
        NettyClient customer = new NettyClient();
        // 创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);
        // 通过代理对象调用服务提供者的方法
        String result = service.hello("你好 RPC");
        log.debug("服务器端返回的结果：{}",result);
    }
}