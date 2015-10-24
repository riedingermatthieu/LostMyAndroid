package fr.enac.lostmyandroid.utilities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.view.AlertAlarmActivity;

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
                Intent intent = new Intent(myContext, AlertAlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent);
                break;
            case "TEXT":
                Intent intent2 = new Intent(myContext, AlertAlarmActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent2);
                break;
            case "VOCAL":
                break;
            default:
                Intent intent3 = new Intent(myContext, AlertAlarmActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myContext.startActivity(intent3);
        }
    }


}
