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

import java.util.Date;
import java.util.EventObject;

import org.apache.camel.management.EventNotifierSupport;
import org.apache.camel.management.event.ExchangeFailedEvent;

/**
 * A custom {@link org.apache.camel.spi.EventNotifier} which is used by
 * Rider Auto Parts to publish failed exchanges to a centralized log server in a custom way.
 *
 * @version $Revision$
 */
public class RiderEventNotifier extends EventNotifierSupport {

    private RiderFailurePublisher publisher;
    private String appId;

    public RiderEventNotifier(String appId) {
        this.appId = appId;
    }

    public void notify(EventObject eventObject) throws Exception {
        // we only want to notify in case of failures
        if (eventObject instanceof ExchangeFailedEvent) {
            notifyFailure((ExchangeFailedEvent) eventObject);
        }
    }

    protected void notifyFailure(ExchangeFailedEvent event) {
        String id = event.getExchange().getExchangeId();
        Exception cause = event.getExchange().getException();
        Date now = new Date();

        publisher.publish(appId, id, now, cause.getMessage());
    }

    public boolean isEnabled(EventObject eventObject) {
        // can be used for fine grained to determine whether to notify this event or not
        return true;
    }

    public void setPublisher(RiderFailurePublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    protected void doStart() throws Exception {
        // here you can initialize services etc
    }

    @Override
    protected void doStop() throws Exception {
        // here you can cleanup services
    }
}
