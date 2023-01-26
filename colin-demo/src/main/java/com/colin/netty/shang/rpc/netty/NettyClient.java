package com.colin.netty.shang.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: colin
 * @Create: 2023/1/26 15:16
 */
public class NettyClient {

    // 创建一个线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler handler;

    // 使用代理模式，获取代理对象
    public Object getBean(final Class<?> serviceClass,final String providerName){

        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass},
                (proxy,method,args) -> {

                    // 客户端每调用一次 hello()，就会进入到此处

                    if (handler == null){
                        initClient();
                    }

                    /**
                     * 设置要发给服务器的信息(由协议头拼接)
                     * providerName：协议头
                     * args[0]：客户端调用 api 的传参，即 hello(xxx)
                     */
                    handler.setPara(providerName + args[0]);
                    return executor.submit(handler).get();
                });
    }

    // 初始化客户端
    private static void initClient(){

        handler = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(handler);
                        }
                    });
            bootstrap.connect("127.0.0.1",8080).sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
