package io.musicmakr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements Messenger.MessageHandler {
    private static final String TAG = MainActivity.class.getName();

    private Button playButton;
    private Button pauseButton;
    private Button prevButton;
    private Button nextButton;
    private Button publishButton;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.play_button:
                    Messenger.getInstance().play();
                    break;
                case R.id.pause_button:
                    Messenger.getInstance().pause();
                    break;
                case R.id.prev_button:
                    Messenger.getInstance().prev();
                    break;
                case R.id.next_button:
                    Messenger.getInstance().next();
                    break;
                case R.id.publish_button:
                    Messenger.getInstance().send("HELLO WORLD.");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(clickListener);
        pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(clickListener);
        prevButton = (Button) findViewById(R.id.prev_button);
        prevButton.setOnClickListener(clickListener);
        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(clickListener);
        publishButton = (Button) findViewById(R.id.publish_button);
        publishButton.setOnClickListener(clickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Messenger.getInstance().init();
        Messenger.getInstance().setMessageHandler(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Messenger.getInstance().finish();
    }

    @Override
    public void onMessage(Object message) {
        Toast.makeText(MyApplication.getInstance(), message.toString(), Toast.LENGTH_SHORT).show();
    }
}
