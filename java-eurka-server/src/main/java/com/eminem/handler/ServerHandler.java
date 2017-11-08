package com.eminem.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

@ChannelHandler.Sharable
public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("server channel active");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf bf =(ByteBuf) msg;
        byte[] bytes = new byte[bf.readableBytes()];
        bf.readBytes(bytes);
        System.out.println("server received data :" + new String(bytes));
        ctx.writeAndFlush(Unpooled.copiedBuffer(new String(bytes).getBytes())).addListener(ChannelFutureListener.CLOSE);//写回数据，
    }


/*    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //flush掉所有写回的数据
                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }*/


    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        ctx.close();//出现异常时关闭channel
    }


}
