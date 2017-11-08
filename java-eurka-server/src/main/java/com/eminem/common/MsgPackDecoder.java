package com.eminem.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx , ByteBuf buf, List<Object> objects) throws Exception {
        final byte[] array;
        final int length = buf.readableBytes();
        array = new byte[length];
        buf.getBytes(buf.readerIndex(),array,0,length);
        MessagePack msgpack = new MessagePack();
        objects.add(msgpack.read(array));
    }

}
