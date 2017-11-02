import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
@ChannelHandler.Sharable
public class ClientHandler extends ChannelHandlerAdapter {
    /**
     *此方法会在连接到服务器后被调用
     * */
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("client channelActive");
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!".getBytes()));
    }
    /**
     *此方法会在接收到服务器数据后调用
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx,  Object msg) throws Exception {
       ByteBuf bf =(ByteBuf) msg;
         byte[] bytes = new byte[bf.readableBytes()];
        bf.readBytes(bytes);
        System.out.println("Client received: " + new String(bytes));
    }
    /**
     *捕捉到异常
     * */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
