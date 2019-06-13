package com.lpframework.netty.netty;

import org.jboss.netty.channel.*;

/**
 *
 * @author lipeng
 * @version Id: MessageHandler.java, v 0.1 2019/6/11 14:03 lipeng Exp $$
 */
public class MessageHandler extends SimpleChannelHandler {
    /**
     * 接收消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("messageReceived");

        String s = (String) e.getMessage();
        System.out.println("服务端收到数据："+s);

        //回写数据给客户端
        ctx.getChannel().write("hello...");
        super.messageReceived(ctx, e);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        super.exceptionCaught(ctx, e);
    }

    /**
     * 获取新连接事件
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected");
        super.channelConnected(ctx, e);
    }

    /**
     * 关闭通道的时候触发 （必须是链接已经建立）
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    /**
     * 通道关闭的时候触发
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }
}
