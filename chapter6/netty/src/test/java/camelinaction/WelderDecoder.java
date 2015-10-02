package camelinaction;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public class WelderDecoder extends MessageToMessageDecoder<ByteBuf> {
    static final int PAYLOAD_SIZE = 8;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.isReadable()) {
            // fill byte array with incoming message
            byte[] bytes = new byte[msg.readableBytes()];
            int readerIndex = msg.readerIndex();
            msg.getBytes(readerIndex, bytes);
            
            // first 7 bytes are the sensor ID, last is the status
            // and the result message will look something like
            // MachineID=2371748;Status=Good
            StringBuilder sb = new StringBuilder();
            sb.append("MachineID=")
            .append(new String(bytes, 0, PAYLOAD_SIZE - 1)).append(";")
            .append("Status=");
            if (bytes[PAYLOAD_SIZE - 1] == '1') {
                sb.append("Good");
            } else {
                sb.append("Failure");
            }
            out.add(sb.toString());
        } else {
            out.add(null);
        }
    }
}
