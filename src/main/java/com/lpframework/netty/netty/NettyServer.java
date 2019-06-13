package com.lpframework.netty.netty;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author lipeng
 * @version Id: NettyServer.java, v 0.1 2019/6/4 14:25 lipeng Exp $$
 */
public class NettyServer {

    private final int port;

    public NettyServer(int port){
        this.port = port;
    }

    public void runServer() throws InterruptedException {
        //	服务类
        ServerBootstrap bootstrap = new ServerBootstrap();

        //	boss线程，主要监听端口和获取worker线程及分配socketChannel给worker线程
        ExecutorService boss = Executors.newCachedThreadPool();
        //	worker线程负责数据读写
        ExecutorService worker = Executors.newCachedThreadPool();

        //	设置niosocket工厂
        bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));

        //	设置管道的工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                // 管道过滤器
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("messageHandler", new MessageHandler());
                return pipeline;
            }
        });

        // 服务类绑定端口
        bootstrap.bind(new InetSocketAddress(port));
        System.out.println("服务端启动...");
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer(8888).runServer();
    }
}
