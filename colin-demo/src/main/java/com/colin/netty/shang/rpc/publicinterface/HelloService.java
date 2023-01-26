package com.colin.netty.shang.rpc.publicinterface;

/**
 * 公共接口，服务提供者和消费者都需要的
 *
 * @author: colin
 * @Create: 2023/1/26 14:34
 */
public interface HelloService {

    String hello(String msg);
}
