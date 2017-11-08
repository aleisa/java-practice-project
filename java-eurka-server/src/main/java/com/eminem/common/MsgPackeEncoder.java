package com.eminem.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class MsgPackeEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object message ,ByteBuf buf) throws Exception {
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(message);
        buf.writeBytes(raw);
    }
}
