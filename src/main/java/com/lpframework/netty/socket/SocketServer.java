package com.lpframework.netty.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lipeng
 * @version Id: SocketServer.java, v 0.1 2019/6/4 9:03 lipeng Exp $$
 */
public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket socket = serverSocket.accept();
        System.out.println("客户端连上了。。。。。");

        byte[] bytes = new byte[1024];
        int len = 0;
        while (true) {
            StringBuffer content =  new StringBuffer();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            //BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream));
            while ((len = inputStream.read(bytes)) != -1) {
                content.append(new String(bytes));
            }
            System.out.println("客户端发来信息："+content);
/*            Scanner scanner =  new Scanner(System.in);
            System.out.println("请输入发送客户端内容：");
            String next = scanner.next();*/
            outputStream.write("你个low逼".getBytes());
            outputStream.close();
            inputStream.close();
            content = null;
            //socket.close();
        }
        //socket.close();

    }
}
