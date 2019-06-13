package com.lpframework.netty.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 *
 * @author lipeng
 * @version Id: PipeTest.java, v 0.1 2019/6/11 9:34 lipeng Exp $$
 */
public class PipeTest {
    @Test
    public void pipe() throws IOException {
        Pipe pipe = Pipe.open();
        //数据写入该通道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        //数据从管道读出
        Pipe.SourceChannel sourceChannel = pipe.source();
        //数据
        String msg = "哈hi的还是大dhahdoah厦回到fdadasdasdsadasdasdasdddddddddddddddddddddddd";
        //缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(msg.getBytes().length);
        //清理缓冲区
        buffer.clear();
        //将数据写入缓冲区
        buffer.put(msg.getBytes());
        //将写模式转换为读模式，可以将数据从缓冲区读到通道
        buffer.flip();
        //将数据从buffer读出，写入到通道
        while (buffer.hasRemaining()){
            sinkChannel.write(buffer);
        }
        //将数据从source读出
        ByteBuffer buffer1 = ByteBuffer.allocate(48);
        int read = sourceChannel.read(buffer1);
        while (read > 0){
            System.out.print(new String(buffer1.array()));
            buffer1.clear();
            read = sourceChannel.read(buffer1);
        }
    }
}
