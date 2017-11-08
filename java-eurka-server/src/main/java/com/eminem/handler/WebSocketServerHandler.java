package com.eminem.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.undertow.websockets.core.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class WebSocketServerHandler  extends SimpleChannelInboundHandler<Object> {
    private static Logger log = LoggerFactory.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("message received");

        if(msg instanceof  FullHttpRequest){
            handlleHttpRequest(ctx,(FullHttpRequest) msg);
        }

        if(msg instanceof TextWebSocketFrame){
            handleWebSocketFrame(ctx,(TextWebSocketFrame) msg);
        }

    }

    @Override
    public void  channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    private void handlleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req){
        if(req.decoderResult().isSuccess()||(!"websocket".equals(req.headers().get("Upgrade")))){

        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8888/ws",null,false);
        handshaker = wsFactory.newHandshaker(req);
        if(handshaker ==null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(),req);
        }

    }

    private static void sendHttpResponse(ChannelHandlerContext ctx , FullHttpRequest req, FullHttpResponse response){
        //返回客户端
        if(response.status().code()!=200){
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();

        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame){
        //判断是否是关闭的链路
/*        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(), ((CloseWebSocketFrame) frame).retain());
            return;
        }
        //是否是Ping消息
        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(((PingWebSocketFrame) frame).content().retain()));
            return;
        }*/

        if(!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s is not supported",frame.getClass().getName()));
        }

        String request = ((TextWebSocketFrame)frame).text();
        log.info(ctx.channel()+" receved "+request);
        ctx.channel().write(new TextWebSocketFrame(new Date().toString()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }

}
