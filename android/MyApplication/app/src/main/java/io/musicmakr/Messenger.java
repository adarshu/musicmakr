package io.musicmakr;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

/**
 * Created by rayho on 5/17/14.
 */
public class Messenger {
    private static Messenger instance;

    public static Messenger getInstance() {
        if (instance == null) {
            instance = new Messenger();
        }
        return instance;
    }

    private static final String TAG = Messenger.class.getName();
    private static final String CHANNEL = "hello_world";

    private Pubnub pubnub;

    private MessageHandler messageHandler;

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    if (messageHandler != null) {
                        messageHandler.onMessage(msg.obj);
                    }
                    break;
            }

            // Clean up to prevent memory leaks
            msg.obj = null;
        }
    };

    private final Callback publishCallback = new Callback() {
        public void successCallback(String channel, final Object response) {
            Log.d(TAG, response.toString());
        }

        public void errorCallback(String channel, final PubnubError error) {
            Log.e(TAG, error.toString());
        }
    };

    private final Callback subscribeCallback = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d(TAG, "SUBSCRIBE : CONNECT on channel:" + channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d(TAG, "SUBSCRIBE : DISCONNECT on channel:" + channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d(TAG, "SUBSCRIBE : RECONNECT on channel:" + channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d(TAG, "SUBSCRIBE : " + channel + " : "
                    + message.getClass() + " : " + message.toString());
            Message m = Message.obtain();
            m.what = 0;
            m.obj = message;
            handler.sendMessage(m);
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.e(TAG, "SUBSCRIBE : ERROR on channel " + channel
                    + " : " + error.toString());
        }
    };

    public void start() {
        if (pubnub != null) {
            Log.e(TAG, "start() was already called");
            return;
        }
        pubnub = new Pubnub(Settings.PUBNUB_PUBLISH_KEY, Settings.PUBNUB_SUBSCRIBE_KEY);
        try {
            pubnub.subscribe(CHANNEL, subscribeCallback);
        } catch (PubnubException e) {
            Log.e(TAG, "Subscribe error", e);
        }
    }

    public void stop() {
        if (pubnub == null) {
            Log.e(TAG, "start() was not called");
            return;
        }
        pubnub.unsubscribeAll();
        pubnub = null;
    }

    public void send(String message) {
        if (pubnub == null) {
            Log.e(TAG, "start() was not called");
            return;
        }
        pubnub.publish(CHANNEL, message, publishCallback);
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public static interface MessageHandler {
        void onMessage(Object message);
    }
}
