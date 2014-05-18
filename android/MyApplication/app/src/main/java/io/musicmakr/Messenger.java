package io.musicmakr;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rayho on 5/17/14.
 */
public class Messenger {
    private static final String TAG = Messenger.class.getName();
    private static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-e877ab56-de0c-11e3-8c07-02ee2ddab7fe";
    private static final String PUBNUB_PUBLISH_KEY = "pub-c-f1c6575f-d2d3-4cd1-bb38-2492c50b7d0e";
    private static final String PUBNUB_SECRET = "sec-c-YzFiZTI4OWUtZjFmMS00Y2IzLTg1ZTYtYWYwMDEwZDllNWRh";
    private static final String PUBNUB_CHANNEL = "hello_world";
    private static final String JSON_CMD = "cmd";
    private static final String JSON_VAL = "val";
    private static Messenger instance;

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

    public static Messenger getInstance() {
        if (instance == null) {
            instance = new Messenger();
        }
        return instance;
    }

    /**
     * Call this to start sending/receiving messages
     */
    public void init() {
        if (pubnub != null) {
            Log.e(TAG, "init() was already called");
            return;
        }
        pubnub = new Pubnub(PUBNUB_PUBLISH_KEY, PUBNUB_SUBSCRIBE_KEY);
        try {
            pubnub.subscribe(PUBNUB_CHANNEL, subscribeCallback);
        } catch (PubnubException e) {
            Log.e(TAG, "Subscribe error", e);
        }
    }

    /**
     * Call this to stop sending/receiving messages
     */
    public void finish() {
        if (pubnub == null) {
            Log.e(TAG, "init() was not called");
            return;
        }
        pubnub.unsubscribeAll();
        pubnub = null;
    }

    public void play() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_CMD, "play");
            send(json);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to create JSON", e);
        }
    }

    public void pause() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_CMD, "pause");
            send(json);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to create JSON", e);
        }
    }

    public void next() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_CMD, "next");
            send(json);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to create JSON", e);
        }
    }

    public void prev() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_CMD, "prev");
            send(json);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to create JSON", e);
        }
    }

    public void search(String query) {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_CMD, "search");
            json.put(JSON_VAL, query);
            send(json);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to create JSON", e);
        }
    }

    /**
     * Send plaintext.
     * @param message
     *      The plaintext message
     */
    public void send(String message) {
        if (pubnub == null) {
            Log.e(TAG, "init() was not called");
            return;
        }
        pubnub.publish(PUBNUB_CHANNEL, message, publishCallback);
    }

    /**
     * Send JSON.
     * @param json
     *      The JSON message
     */
    public void send(JSONObject json) {
        if (pubnub == null) {
            Log.e(TAG, "init() was not called");
            return;
        }
        pubnub.publish(PUBNUB_CHANNEL, json, publishCallback);
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public static interface MessageHandler {
        void onMessage(Object message);
    }
}
