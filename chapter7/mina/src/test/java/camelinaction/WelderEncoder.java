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

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

class WelderEncoder implements ProtocolEncoder {
    static final int PAYLOAD_SIZE = 8;
    
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out)
        throws Exception {             
        ByteBuffer buf = ByteBuffer.allocate(PAYLOAD_SIZE);
        String s = (String) message;                    
        buf.put(s.getBytes());
        buf.flip();
        out.write(buf);
    }

    public void dispose(IoSession ioSession) throws Exception {
        // do nothing
    }
}