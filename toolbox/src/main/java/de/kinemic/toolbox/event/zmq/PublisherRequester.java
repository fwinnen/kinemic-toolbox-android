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

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;
import org.zeromq.ZMQ;
import org.zeromq.ZSocket;

/**
 * This class is used to send requests back to the publisher.
 * Currently there is only one request, which resets the orientation reference for the airmouse feature.
 */
public class PublisherRequester {

    private HandlerThread mIOThread;
    private IOHandler mIOHandler;

    /**
     * Start the requester for a publisher at 'localhost'.
     * The requester can be started again after it was stopped.
     */
    public void start() {
        start("localhost");
    }

    /**
     * Start the requester for a publisher at the given ip address.
     * The requester can be started again after it was stopped.
     * @param ip the ip address of the publisher like '127.0.0.1'
     */
    public void start(String ip) {
        if (mIOThread == null) {
            mIOThread = new HandlerThread("requesterIO");
            mIOThread.start();
            mIOHandler = new IOHandler(ip, mIOThread.getLooper());
            mIOHandler.obtainMessage(IOHandler.MSG_START).sendToTarget();
        }
    }

    /**
     * Stop the requester.
     * The requester can be started again after it was stopped.
     */
    public void stop() {
        if (mIOThread != null) {
            mIOHandler.obtainMessage(IOHandler.MSG_TERM).sendToTarget();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mIOThread.quitSafely();
            } else {
                mIOThread.quit();
            }
            mIOThread = null;
            mIOHandler = null;
        }
    }

    /**
     * Request a orientation reset.
     * The reset is used to reset the reference for the airmouse feature.
     * Use this method when your arm is not moving to set a reference for further relative
     * {@link de.kinemic.toolbox.event.PublisherEvent.MouseEvent}.
     */
    public void requestOrientationReset() {
        if (mIOThread != null) {
            mIOHandler.obtainMessage(IOHandler.MSG_REQUEST_ORIENTATION).sendToTarget();
        }
    }

    private static class IOHandler extends Handler {

        private static final int MSG_REQUEST_ORIENTATION = 1;
        private static final int MSG_TERM = 2;
        private static final int MSG_START = 3;

        private Handler mHandler;
        private ZSocket mPublisher;
        private final String mIP;

        IOHandler(String ip, Looper looper) {
            super(looper);
            mIP = ip;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_START:
                    mPublisher = new ZSocket(ZMQ.PUB);
                    mPublisher.connect("tcp://" + mIP + ":9998");
                    break;
                case MSG_REQUEST_ORIENTATION:
                    try {
                        JSONObject request = new JSONObject();
                        request.put("type", "OrientationReset");
                        request.put("payload", null);
                        mPublisher.sendStringUtf8(request.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_TERM:
                    mPublisher.close();
                    break;
            }
        }
    }
}
