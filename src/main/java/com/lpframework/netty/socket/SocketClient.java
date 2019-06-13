package com.lpframework.netty.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author lipeng
 * @version Id: SocketClient.java, v 0.1 2019/6/4 9:43 lipeng Exp $$
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8888);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        while (true){
            //发送消息
            Scanner scanner =  new Scanner(System.in);
            System.out.println("请输入发送服务端内容：");
            String next = scanner.next();
            outputStream.write(next.getBytes());
            outputStream.close();

            //接收消息
            StringBuffer content = new StringBuffer();
            while ((len = inputStream.read(bytes)) != -1){
                content.append(new String(bytes));
            }
            System.out.println("服务端发来信息："+content);
            content = null;

            inputStream.close();
        }
        //socket.close();
    }
}
