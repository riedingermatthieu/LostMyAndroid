package fr.enac.lostmyandroid.views;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.enac.lostmyandroid.R;

public class AlertAlarmActivity extends AppCompatActivity {

    private Integer lockCode; // Pour le code de sécurité TODO à revoir
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_alarm);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
          //      audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        String message = getIntent().getStringExtra("message");

        TextView textView = (TextView) findViewById(R.id.TextView_message_alerte);

        if (message != null && !message.isEmpty())
            textView.setText(message);

        playSound(R.raw.alarm);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayer.stop();
    }

    private void playSound(int resId) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(this, resId);
        mPlayer.start();
    }

}
