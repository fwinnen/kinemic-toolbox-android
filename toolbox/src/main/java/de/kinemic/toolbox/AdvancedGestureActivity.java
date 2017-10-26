/*
 * Copyright (C) 2017 Kinemic GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kinemic.toolbox;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

import de.kinemic.toolbox.event.PublisherEvent;
import de.kinemic.toolbox.event.PublisherLog;
import de.kinemic.toolbox.event.zmq.PublisherListener;
import de.kinemic.toolbox.event.zmq.PublisherRequester;

/**
 * This template activity uses the zmq PublisherListener to receive events.
 * Use {@link #getEventTypes()} to implement a custom event filter.
 * Override {@link #handleEvent(PublisherEvent)} to handle gesture events.
 */
public class AdvancedGestureActivity extends AppCompatActivity {
    private final PublisherListener mReceiver = new PublisherListener() {

        @Override
        protected void handleLog(PublisherLog log) {

        }

        @Override
        protected void handleEvent(PublisherEvent base) {
            try {
                AdvancedGestureActivity.this.handleEvent(base);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private final PublisherRequester mRequester = new PublisherRequester();

    /**
     * Event types to receive. Override this method to implement a custom filter.
     * @return Array of event types to receive. [""] for all
     */
    protected String[] getEventTypes() {
        return new String[] {""};
    }

    /**
     * Return the ip address as a string. (default: localhost)
     * Override this method to connect to a remote Publisher.
     * @return publisher ip as string (i.e. '127.0.0.1')
     */
    protected String getPublisherIP() {
        return "localhost";
    }

    @Override
    protected void onResume() {
        mReceiver.setEventTypes(getEventTypes());
        mReceiver.start(getPublisherIP());
        mRequester.start(getPublisherIP());
        super.onResume();
    }

    @Override
    protected void onPause() {
        mReceiver.stop();
        mRequester.stop();
        super.onPause();
    }

    protected void requestOrientationReset() {
        mRequester.requestOrientationReset();
    }

    /**
     * Called for every received event. Override this method to implement gestures.
     * @param event the {@link PublisherEvent}
     * @throws JSONException can throw if event's parameters are not valid
     */
    protected void handleEvent(PublisherEvent event) throws JSONException {

    }
}
