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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Java class which represents any kind of event from the publisher.
 * Use the {@link #asGesture()}, {@link #asActivation()}, ... methods depending on the Type
 * to get the specified event.
 */
public class PublisherEvent {
    /**  Action representing a event broadcast.  */
    public static final String ACTION_EVENT = "de.kinemic.publisher.ACTION.EVENT";

    /** Extra key for the type in a event broadcast. */
    public static final String BROADCAST_TYPE = "type";

    /** Extra key for the json representing the event in a event broadcast. */
    public static final String BROADCAST_JSON = "json";

    public static final String GESTURE_ROTATE_RL = "Rotate RL";
    public static final String GESTURE_ROTATE_LR = "Rotate LR";
    public static final String GESTURE_ROTATE_R = "Rotate R";
    public static final String GESTURE_ROTATE_L = "Rotate L";
    public static final String GESTURE_CIRCLE_R = "Circle R";
    public static final String GESTURE_CIRCLE_L = "Circle L";
    public static final String GESTURE_SWIPE_R = "Swipe R";
    public static final String GESTURE_SWIPE_L = "Swipe L";
    public static final String GESTURE_SWIPE_UP = "Swipe Up";
    public static final String GESTURE_SWIPE_DOWN = "Swipe Down";
    public static final String GESTURE_WIRL_R = "Wirl R";
    public static final String GESTURE_WIRL_L = "Wirl L";
    public static final String GESTURE_EARTOUCHL_R = "Eartouch R";
    public static final String GESTURE_EARTOUCH_L = "Eartouch L";
    public static final String GESTURE_CHESTTOUCH = "Chesttouch";
    public static final String GESTURE_CHECK_MARK = "Check Mark";
    public static final String GESTURE_X_MARK = "X Mark";
    public static final String GESTURE_TAP = "Tap";
    public static final String GESTURE_DOUBLE_TAP = "DoubleTap";

    /**
     * Different Types of publisher events.
     */
    public static enum Type {
        Gesture("Gesture"), Writing("Writing"), MouseEvent("MouseEvent"), Activation("Activation"), WritingSegment("WritingSegment"), Heartbeat("Heartbeat");

        /** The type as used in the json event. */
        public final String jsonType;

        Type(String jsonType) {
            this.jsonType = jsonType;
        }
    }

    private static final Map<String, Type> sTypeMap;
    static {
        Map<String, Type> aMap = new HashMap<>();
        aMap.put("Gesture", Type.Gesture);
        aMap.put("Writing", Type.Writing);
        aMap.put("MouseEvent", Type.MouseEvent);
        aMap.put("MouseToggle", Type.MouseEvent);
        aMap.put("Activation", Type.Activation);
        aMap.put("WritingSegment", Type.WritingSegment);
        aMap.put("Heartbeat", Type.Heartbeat);
        sTypeMap = Collections.unmodifiableMap(aMap);
    }

    /**
     * The {@link Type} of the event.
     */
    public final Type type;

    /**
     * Parameters of the event as a json object.
     */
    public final JSONObject parameters;

    private PublisherEvent(Type type, JSONObject parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    /**
     * Parse a event from json.
     * @param json json object
     * @return the parsed json as {@link PublisherEvent}
     * @throws JSONException if the json object could not be parsed.
     */
    public static @NonNull
    PublisherEvent fromJson(@NonNull JSONObject json) throws JSONException {
        final String type_s = json.getString("type");
        final JSONObject params = json.getJSONObject("parameters");

        final Type type = sTypeMap.get(type_s);
        if (type != null) return new PublisherEvent(type, params);
        throw new JSONException("Unexpected type: " + type_s);
    }

    /**
     * Parse a event from json string.
     * @param json json string
     * @return the parsed json as {@link PublisherEvent}
     * @throws JSONException if the json object could not be parsed.
     */
    public static @NonNull
    PublisherEvent fromJson(@NonNull String json) throws JSONException {
        return fromJson(new JSONObject(json.replace("null", "{}")));
    }

    /**
     * Serialize this instance in json.
     * @return this object as json.
     * @throws JSONException if instance was invalid
     */
    public JSONObject toJson() throws JSONException {
        return new JSONObject()
                .put("type", type.jsonType)
                .put("parameters", parameters);
    }

    /**
     * Transform this event to a {@link Gesture}.
     * @return this event as a {@link Gesture}.
     * @throws JSONException if this instance is not a valid {@link Gesture}.
     */
    public Gesture asGesture() throws JSONException {
        return Gesture.from(this);
    }

    /**
     * Transform this event to a {@link Writing}.
     * @return this event as a {@link Writing}.
     * @throws JSONException if this instance is not a valid {@link Writing}.
     */
    public Writing asWriting() throws JSONException {
        return Writing.from(this);
    }

    /**
     * Transform this event to a {@link WritingSegment}.
     * @return this event as a {@link WritingSegment}.
     * @throws JSONException if this instance is not a valid {@link WritingSegment}.
     */
    public WritingSegment asWritingSegment() throws JSONException {
        return WritingSegment.from(this);
    }

    /**
     * Transform this event to a {@link Activation}.
     * @return this event as a {@link Activation}.
     * @throws JSONException if this instance is not a valid {@link Activation}.
     */
    public Activation asActivation() throws JSONException {
        return Activation.from(this);
    }

    /**
     * Transform this event to a {@link Heartbeat}.
     * @return this event as a {@link Heartbeat}.
     * @throws JSONException if this instance is not a valid {@link Heartbeat}.
     */
    public Heartbeat asHeartbeat() throws JSONException {
        return Heartbeat.from(this);
    }

    /**
     * Transform this event to a {@link MouseEvent}.
     * @return this event as a {@link MouseEvent}.
     * @throws JSONException if this instance is not a valid {@link MouseEvent}.
     */
    public MouseEvent asMouseEvent() throws JSONException {
        return MouseEvent.from(this);
    }

    /**
     * A gesture event, triggered by a gesture command.
     */
    public static class Gesture {
        /** The name of the gesture */
        public final String name;

        private Gesture(String name) {
            this.name = name;
        }

        private static Gesture from(PublisherEvent base) throws JSONException {
            return new Gesture(base.parameters.getString("name"));
        }
    }

    /**
     * A writing event, triggered by airwriting.
     */
    public static class Writing {
        /** The vocabulary used by the publisher */
        public final String vocabulary;
        /** The hypothesis for the current segment */
        public final String hypothesis;
        /** Whether the hypothesis is the final one for the segment (the segment has ended). */
        public final boolean isFinal;

        private Writing(String vocabulary, String hypothesis, boolean isFinal) {
            this.vocabulary = vocabulary;
            this.hypothesis = hypothesis;
            this.isFinal = isFinal;
        }

        private static Writing from(PublisherEvent base) throws JSONException {
            return new Writing(
                    base.parameters.getString("vocabulary"),
                    base.parameters.getString("hypothesis"),
                    base.parameters.getBoolean("final"));
        }
    }

    /**
     * An event, triggered at the start and end of a airwriting segment.
     */
    public static class WritingSegment {
        /** Whether the segment has ended or started */
        public final boolean started;

        private WritingSegment(boolean started) {
            this.started = started;
        }

        private static WritingSegment from(PublisherEvent base) throws JSONException {
            return new WritingSegment(
                    base.parameters.getBoolean("started"));
        }
    }

    /**
     * An activation event, triggered py pausing and resuming the streaming of the sensor.
     */
    public static class Activation {
        /** Whether the state changed to active or inactive */
        public final boolean active;

        private Activation(boolean active) {
            this.active = active;
        }

        private static Activation from(PublisherEvent base) throws JSONException {
            return new Activation(base.parameters.getBoolean("active"));
        }
    }

    /**
     * A heartbeat event, triggered periodically to inform about the current state of the publisher.
     */
    public static class Heartbeat {
        /** Whether the publisher is active */
        public final boolean active;
        /** The flags defining the enabled functionality of the publisher */
        public final int flags;
        /** The address of the current sensor stream */
        public final String stream;
        /** The name of the last connected sensor */
        public final String sensor;
        /** The time in seconds since the last sensor message */
        public final long last;

        private Heartbeat(boolean active, int flags, String stream, String sensor, long last) {
            this.active = active;
            this.flags = flags;
            this.stream = stream;
            this.sensor = sensor;
            this.last = last;
        }

        private static Heartbeat from(PublisherEvent base) throws JSONException {
            return new Heartbeat(
                    base.parameters.getBoolean("active"),
                    base.parameters.getInt("flags"),
                    base.parameters.getString("stream"),
                    base.parameters.getString("sensor"),
                    base.parameters.getLong("last"));
        }
    }

    /**
     * A mouse event, triggered by the airmouse feature.
     */
    public static class MouseEvent {
        /**
         * Type of MouseEvent, toggle or move.
         * Toggle is triggered by a quick rotation to the right and back.
         * Move events are triggered if the hand moved (with a threshold).
         */
        public enum Type { Toggle, Move }

        /**
         * Type of MouseEvent, toggle or move.
         * See {@link Type}.
         */
        public final Type type;
        /** Movement in x direction as delta. */
        public final double dx;
        /** Movement in y direction as delta. */
        public final double dy;
        /** Whether the hand is currently in a vertical rotated position. (Rotated 90° to the right) */
        public final boolean palmVertical;

        /**
         * Crate a new MouseEvent.
         * TODO: This method should not be needed and be removed.
         */
        public MouseEvent(Type type, double dx, double dy, boolean palmVertical) {
            this.type = type;
            this.dx = dx;
            this.dy = dy;
            this.palmVertical = palmVertical;
        }

        private static MouseEvent from(PublisherEvent base) throws JSONException {
            if (!base.parameters.has("type")) return new MouseEvent(Type.Toggle, 0.0, 0.0, false);

            String type_s = base.parameters.getString("type");
            if ("move".equals(type_s)) {
                return new MouseEvent(Type.Move, base.parameters.getDouble("dx"), base.parameters.getDouble("dy"), base.parameters.getBoolean("down"));
            } else if ("toggle".equals(type_s)) {
                return new MouseEvent(Type.Toggle, 0.0, 0.0, false);
            } else {
                throw new JSONException("Invalid MouseEvent type: " + type_s);
            }
        }
    }
}
