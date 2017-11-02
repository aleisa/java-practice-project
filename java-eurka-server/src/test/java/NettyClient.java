import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyClient {
    private final String host;
    private final int port;

    public NettyClient(String host,Integer port){
        this.host = host;
        this.port =port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.remoteAddress(new InetSocketAddress(host, port));
        b.handler(new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        ChannelFuture cf = b.connect().sync();
        ChannelFuture cf2 = b.connect("127.0.0.1",8889);
        cf2.channel().writeAndFlush(Unpooled.copiedBuffer("hello world".getBytes()));
        cf.addListener(new ChannelFutureListener() {

            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("client connected");
                }else{
                    System.out.println("server attemp failed");
                    future.cause().printStackTrace();
                }

            }
        });
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

    public static void main(String args[]) throws Exception {
        System.setProperty("io.netty.noUnsafe","true");
        new NettyClient("127.0.0.1",8888).run();
    }
}
