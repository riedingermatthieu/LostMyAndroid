package fr.enac.lostmyandroid.Controllers;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.utilities.MyAdminReceiver;

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


    // Administration Data
    private static final int ADMIN_INTENT = 15;
    private static final String description = "Sample Administrator description";
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    public AlertAlarmActivityController(Activity mActivity, String msg) {
        myActivity = mActivity;
        message = msg;
        audioManager = (AudioManager) myActivity.getSystemService(Context.AUDIO_SERVICE);
        textView = (TextView) myActivity.findViewById(R.id.TextView_message_alerte);
        stopButton = (Button) myActivity.findViewById(R.id.button);

        registerStopButtonListener();
        initAdminComponents();
        enableAdminMode();

    }

    public void registerStopButtonListener() {
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO envoyer vers le lockscreen, s'il Unlock on arrête l'alarme

                mPlayer.stop();
                myActivity.finish();

                /*
                Intent intent = new Intent(myActivity.getApplicationContext(), LockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myActivity.getApplicationContext().startActivity(intent);
*/
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
        lockPhone();
        mPlayer.stop();
    }

    public void putMaxSound() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }


    /* Administration */

    public void initAdminComponents() {
        mDevicePolicyManager = (DevicePolicyManager)myActivity.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(myActivity, MyAdminReceiver.class);
    }

    public void enableAdminMode() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
        myActivity.startActivityForResult(intent, ADMIN_INTENT);
    }

    public void lockPhone() {
        boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
        if (isAdmin) {
            mDevicePolicyManager.lockNow();
        }else{
            Toast.makeText(myActivity.getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
        }
    }



}
