package com.lpframework.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author lipeng
 * @version Id: NioServer.java, v 0.1 2019/6/6 10:18 lipeng Exp $$
 */
public class NioServer {

    private Selector selector;
    private int port;

    public NioServer(int port){
        this.port = port;
    }

    public void initServer() {
        try {
            //创建一个选择器
            this.selector = Selector.open();
            //创建一个ServerSocketChannel通道
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
            //通道设置为非阻塞
            channel.configureBlocking(false);
            //附加数据
            Map<String,String> map = new HashMap<String,String>();
            map.put("name","李鹏");
            //将通道注册到选择器上
            channel.register(selector, SelectionKey.OP_ACCEPT,map);
            System.out.println("服务器启动成功，等待客户端连接。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accept(){
        try {
            while (true){
                //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
                this.selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for (SelectionKey selectionKey:selectionKeys) {
                    //移除注册的事件
                    selectionKeys.remove(selectionKey);

                    if(selectionKey.isAcceptable()){
                        //获取ServerSocketChannel
                        ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                        //接收客户端
                        SocketChannel channel = socketChannel.accept();
                        System.out.println("服务端接收客户端成功。。。。");
                        channel.configureBlocking(false);
                        //注册数据读取事件
                        channel.register(selector,SelectionKey.OP_READ);
                    }else if(selectionKey.isReadable()){
                        Thread.sleep(100);
                        //获取注册读取的通道
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        channel.configureBlocking(false);

                        //创建缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取数据
                        int read = channel.read(buffer);
                        System.err.print("李鹏：");
                        while (read > 0){
                            System.out.print(new String(buffer.array()));
                            buffer.clear();
                            read = channel.read(buffer);
                        }
                        System.out.println("");
                        channel.register(selector,SelectionKey.OP_WRITE);
                    }else if(selectionKey.isWritable()){
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
                        channel.register(selector,SelectionKey.OP_READ);
                    }else {

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioServer server = new NioServer(8888);
        server.initServer();
        server.accept();
    }
}
