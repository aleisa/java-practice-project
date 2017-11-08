package com.eminem.server;

import com.eminem.common.MsgPackDecoder;
import com.eminem.common.MsgPackeEncoder;
import com.eminem.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class EchoClient {
    private int port;

    private int sendNumber;

    public EchoClient(int port,int sendNumber){
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("msgpack decoder",new MsgPackDecoder())
                                    .addLast("msgpack encoder",new MsgPackeEncoder())
                                    .addLast(new EchoClientHandler(10));
                        }
                    });
        }finally {
            group.shutdownGracefully();
        }
    }

}
