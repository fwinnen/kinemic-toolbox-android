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

package de.kinemic.toolbox.event;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Java class which represents a log message received from the publisher.
 */
public class PublisherLog {
    /**  Action representing a log broadcast.  */
    public static final String ACTION_LOG = "de.kinemic.publisher.ACTION.LOG";

    /** Extra key for the level in a log broadcast */
    public static final String BROADCAST_LEVEL = "level";

    /** Extra key for the json representing the log in a log broadcast */
    public static final String BROADCAST_JSON = "json";

    /**
     * Name of the logger who send the log.
     */
    public final @NonNull String who;

    /**
     * Log level as a string.
     * One of: 'debug', 'info', 'warn', 'error'.
     */
    public final @NonNull String level;

    /**
     * The log message.
     */
    public final @NonNull String message;

    /**
     * The timestamp of the log creation in milliseconds since epoch.
     */
    public final long timestamp;

    private PublisherLog(@NonNull String who, @NonNull String level, @NonNull String message, long timestamp) {
        this.who = who;
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Parse a log from json.
     * @param json json object
     * @return the parsed json as {@link PublisherLog}
     * @throws JSONException if the json object could not be parsed.
     */
    public static @NonNull PublisherLog fromJson(JSONObject json) throws JSONException {
        JSONObject parameters = json.getJSONObject("parameters");
        return new PublisherLog(parameters.getString("who"), parameters.getString("level"), parameters.getString("message"), parameters.getLong("timestamp"));
    }

    /**
     * Parse a log from json string.
     * @param json json string
     * @return the parsed json as {@link PublisherLog}
     * @throws JSONException if the json string could not be parsed.
     */
    public static @NonNull PublisherLog fromJson(String json) throws JSONException {
        return fromJson(new JSONObject(json));
    }
}
