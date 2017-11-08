package com.eminem.server;

import com.eminem.handler.ServerHandler;
import com.eminem.handler.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyServer {
    private static final int port = 8888;
    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();// 引导辅助程序
        EventLoopGroup bossGroup = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            b.group(bossGroup,workGroup);
            b.channel(NioServerSocketChannel.class);// 设置nio类型的channel
            b.childHandler(new ChannelInitializer<SocketChannel>() {//有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception {
                    // pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
                    ch.pipeline()
                            .addLast("http-codec",new HttpServerCodec())
                            .addLast("aggregator",new HttpObjectAggregator(65536))
                            .addLast("http-chunked",new ChunkedWriteHandler())

                    .addLast(new WebSocketServerHandler());
                }
            });

            ChannelFuture f = b.bind(8888).sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
           /* ChannelFuture f2 = b.bind(8889).sync();*/

            System.out.println(NettyServer.class.getName() + " started and listen on " + f.channel().localAddress());

            f.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭
          /*  f2.channel().closeFuture().sync();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully().sync();//关闭EventLoopGroup，释放掉所有资源包括创建的线程
            workGroup.shutdownGracefully().sync();
        }
    }
    public static void main(String[] args) {
        System.setProperty("io.netty.noUnsafe","true");
        try {
            new NettyServer().start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
