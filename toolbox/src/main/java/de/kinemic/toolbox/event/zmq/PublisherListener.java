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

package de.kinemic.toolbox.event.zmq;

import android.util.Log;

import org.json.JSONException;

import de.kinemic.toolbox.event.PublisherEvent;
import de.kinemic.toolbox.event.PublisherLog;

/**
 * This class can be used to receive Publisher events over the network (also locally).
 * It wraps the zmq json receiver and delivers java object events.
 * Extend this class and override the {@link #handleLog(PublisherLog)} and {@link #handleEvent(PublisherEvent)}
 * Methods.
 */
public abstract class PublisherListener extends PublisherJsonListener {
    private static final String TAG = PublisherListener.class.getSimpleName();

    /**
     * Called for every received log message.
     * @param log the received {@link PublisherLog}
     */
    protected abstract void handleLog(PublisherLog log);

    /**
     * Called for every received event.
     * @param base the received {@link PublisherEvent}
     */
    protected abstract void handleEvent(PublisherEvent base);

    @Override
    protected final void handleLog(String level, String json) {
        try {
            handleLog(PublisherLog.fromJson(json));
        } catch (JSONException e) {
            Log.w(TAG, "Could not parse json event: " + json, e);
        }
    }

    @Override
    protected final void handleEvent(String type, String json) {
        try {
            handleEvent(PublisherEvent.fromJson(json));
        } catch (JSONException e) {
            Log.w(TAG, "Could not parse json event: " + json, e);
        }
    }
}
