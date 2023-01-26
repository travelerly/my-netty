package com.colin.netty.shang.rpc.provider;

import com.colin.netty.shang.rpc.netty.NettyServer;

/**
 * ServerBootstrap 会启动一个服务提供者，其实就是 Netty Server
 * @author: colin
 * @Create: 2023/1/26 14:39
*/public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",8080);
    }
}
