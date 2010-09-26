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
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

class WelderDecoder extends CumulativeProtocolDecoder {
    static final int PAYLOAD_SIZE = 8;
    
    protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (in.remaining() >= PAYLOAD_SIZE) {
            byte[] buf = new byte[in.remaining()];
            in.get(buf);
            
            // first 7 bytes are the sensor ID, last is the status
            // and the result message will look something like
            // MachineID=2371748;Status=Good
            StringBuilder sb = new StringBuilder();
            sb.append("MachineID=")
            .append(new String(buf, 0, PAYLOAD_SIZE - 1)).append(";")
            .append("Status=");
            if (buf[PAYLOAD_SIZE - 1] == '1') {
                sb.append("Good");
            } else {
                sb.append("Failure");
            }
            out.write(sb.toString());
            return true;
        } else {
            return false;
        }
    }
}