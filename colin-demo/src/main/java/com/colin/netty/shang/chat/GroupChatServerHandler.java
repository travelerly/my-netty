package com.colin.netty.shang.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * @author: colin
 * @Create: 2023/1/22 14:11
 */
@Slf4j
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 定义一个 channel 组，有 netty提供，用于管理所有的 channel
     * GlobalEventExecutor.INSTANCE：全局事件执行器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 连接建立事件，一旦连接建立，首先调用此方法
     * 将当前 channel 加入到 ChannelGroup
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        /**
         * 将 xxx 客户端加入到聊天的信息，推送给其它客户端
         * writeAndFlush()：该方法会将 channelGroup 中的所有 channel 遍历一遍，并向其发送消息，无需手动遍历
         */
        channelGroup.writeAndFlush("[客户端] " + channel.remoteAddress()+" 加入聊天 " + sdf.format(new java.util.Date()) + "\n");
        channelGroup.add(ctx.channel());
        log.debug("当前 channelGroup 大小为 {}",channelGroup.size());
    }

    /**
     * 断开连接事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将 xxx 客户端离开的信息推送给当前在前的其它客户端
        channelGroup.writeAndFlush("[客户端] "+ channel.remoteAddress()+" 离开了 " + sdf.format(new java.util.Date()) + "\n");
        log.debug("当前 channelGroup 大小为 {}",channelGroup.size());
    }

    /**
     * channel 处于活动状态
     * 提示 xxx 客户端上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("客户端 {} 上线了~",ctx.channel());
    }

    /**
     * channel 处于非活动状态
     * 提示 xxx 客户端离线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("客户端 {} 离线了~",ctx.channel());
    }

    /**
     * 读取数据事件
     * 读取消息，处理后转发消息
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        // 遍历 channelGroup，根据不同情况，回复不同消息
        channelGroup.forEach((ch)->{
            if (ch != channel){
                // 说明遍历到的 channel 并非当前消息来源的 channel，需将消息转发给遍历到的 channel
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送消息 " + msg + sdf.format(new java.util.Date()) + "\n");
            } else {
                // 说明当前遍历到的 channel 就是消息来源的 channel，即是回显自己发送的消息
                ch.writeAndFlush("[自己] 发送了消息 " + msg + sdf.format(new java.util.Date()) + "\n");
            }
        });
    }

    /**
     * 异常事件
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 发生异常，关闭通道
        ctx.channel().close();
    }
}
