/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

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
