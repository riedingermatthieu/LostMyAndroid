package fr.enac.lostmyandroid;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private MediaPlayer mPlayer = null;
    private Context myContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        myContext = context;

        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsMessageStr = "";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();

                traiteMessage(smsBody);
            }

        }
    }

    private void traiteMessage(String smsBody) {

        switch (smsBody) {
            case "RING":
                playSound(R.raw.alarm);
                break;
            case "TEXT":
            case "VOCAL":
        }
    }

    private void playSound(int resId) {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(myContext, resId);
        mPlayer.start();
    }
}
