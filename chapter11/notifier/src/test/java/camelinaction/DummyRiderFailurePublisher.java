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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * For testing purposes only, hence why its a bit rude how its coded.
 *
 * @version $Revision$
 */
public class DummyRiderFailurePublisher implements RiderFailurePublisher {

    private List<Event> events = new ArrayList<Event>();

    public class Event {
        public String appId;
        public String eventId;
        public Date timestamp;
        public String message;
    }

    public void publish(String appId, String eventId, Date timestamp, String message) {
        Event event = new Event();
        event.appId = appId;
        event.eventId = eventId;
        event.timestamp = timestamp;
        event.message = message;

        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }
}
