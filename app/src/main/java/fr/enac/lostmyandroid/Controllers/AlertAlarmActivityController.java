package fr.enac.lostmyandroid.Controllers;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.enac.lostmyandroid.R;

/**
 * Created by Amine on 08/11/2015.
 */
public class AlertAlarmActivityController {
    private MediaPlayer mPlayer = null;
    private Activity myActivity;
    private String message;
    private TextView textView;
    private Button stopButton;
    private AudioManager audioManager;

    public AlertAlarmActivityController(Activity mActivity, String msg) {
        myActivity = mActivity;
        message = msg;
        audioManager = (AudioManager) myActivity.getSystemService(Context.AUDIO_SERVICE);
        textView = (TextView) myActivity.findViewById(R.id.TextView_message_alerte);
        stopButton = (Button) myActivity.findViewById(R.id.button);

        registerStopButtonListener();
    }

    public void registerStopButtonListener() {
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO envoyer vers le lockscreen, s'il Unlock on arrête l'alarme

                mPlayer.stop();
                myActivity.finish();

            }
        });
    }

    public void showMessage() {
        if (message != null && !message.isEmpty())
            textView.setText(message);
    }

    public void playSound(int resId) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(myActivity, resId);
        mPlayer.start();
    }

    public void stopSound() {
        mPlayer.stop();
    }

    public void putMaxSound() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

}
