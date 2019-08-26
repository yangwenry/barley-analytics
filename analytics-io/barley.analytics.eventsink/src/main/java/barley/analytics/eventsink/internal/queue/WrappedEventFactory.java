/*
*  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package barley.analytics.eventsink.internal.queue;

import com.lmax.disruptor.EventFactory;

import barley.databridge.commons.Event;

public class WrappedEventFactory implements EventFactory<WrappedEventFactory.WrappedEvent> {

    public WrappedEvent newInstance() {
        return new WrappedEvent();
    }

    public static class WrappedEvent {

        private Event event;
        private int size;

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        @Override
        public String toString() {
            return "WrappedEvent{" +
                    "event=" + event +
                    "}";
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

}
