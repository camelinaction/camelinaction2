package camelinaction;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class WelderEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(msg.length());
        buf.writeBytes(msg.getBytes());
        out.add(buf);
    }
}
