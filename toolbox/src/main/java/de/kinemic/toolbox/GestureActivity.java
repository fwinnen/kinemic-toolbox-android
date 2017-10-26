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

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

import de.kinemic.toolbox.event.PublisherEvent;
import de.kinemic.toolbox.event.broadcast.PublisherEventReceiver;

/**
 * This template activity uses the broadcast Publisher listener to receive events.
 * Override {@link #handleEvent(PublisherEvent)} to handle gesture events.
 */
public class GestureActivity extends AppCompatActivity {
    private final PublisherEventReceiver mReceiver = new PublisherEventReceiver() {
        @Override
        protected void handleEvent(PublisherEvent event) {
            GestureActivity.this.handleEvent(event);
        }
    };

    @Override
    protected void onResume() {
        registerReceiver(mReceiver, new IntentFilter(PublisherEventReceiver.ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    /**
     * Called for every received event. Override this method to implement gestures.
     * @param event the {@link PublisherEvent}
     */
    protected void handleEvent(PublisherEvent event) {

    }
}
