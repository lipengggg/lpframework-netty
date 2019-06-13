package com.lpframework.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author lipeng
 * @version Id: NioClient.java, v 0.1 2019/6/10 16:08 lipeng Exp $$
 */
public class NioClient {
    private Selector selector;
    private String host;
    private int port;

    public NioClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public void initClient(){
        try {
            //创建Selector
            this.selector = Selector.open();
            //创建socket通道
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));
            //注册写事件
            channel.register(selector, SelectionKey.OP_CONNECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accept(){
        try {
            while (true){
                this.selector.select();
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();

                for (SelectionKey selectionKey:selectionKeys) {
                    //移除注册的事件
                    selectionKeys.remove(selectionKey);

                    if (selectionKey.isWritable()){
                        Thread.sleep(100);
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        channel.configureBlocking(false);
                        //System.out.println("");
                        System.err.print("我：");
                        Scanner scanner = new Scanner(System.in);
                        String msg = scanner.nextLine();
                        ByteBuffer buffer = ByteBuffer.allocate(msg.getBytes().length);
                        buffer.clear();
                        buffer.put(msg.getBytes());
                        buffer.flip();
                        channel.write(buffer);
                        //注册读事件
                        channel.register(selector,SelectionKey.OP_READ);
                    }else if(selectionKey.isReadable()){
                        Thread.sleep(100);
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        channel.configureBlocking(false);

                        //创建缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取数据
                        int read = channel.read(buffer);
                        System.err.print("梁妃连：");
                        while (read > 0){
                            System.out.print(new String(buffer.array()));
                            buffer.clear();
                            read = channel.read(buffer);
                        }
                        System.out.println("");
                        //注册读事件
                        channel.register(selector,SelectionKey.OP_WRITE);
                    }else if(selectionKey.isConnectable()){
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        channel.configureBlocking(false);

                        // 如果正在连接，则完成连接
                        if(channel.isConnectionPending()){
                            channel.finishConnect();
                        }
                        channel.register(selector,SelectionKey.OP_WRITE);
                    }else {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioClient client = new NioClient("127.0.0.1",8888);
        client.initClient();
        client.accept();
    }
}
