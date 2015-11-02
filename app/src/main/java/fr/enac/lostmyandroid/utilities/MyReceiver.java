package fr.enac.lostmyandroid.utilities;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.view.AlertAlarmActivity;
import fr.enac.lostmyandroid.view.MainActivity;
import fr.enac.lostmyandroid.view.MapsActivity;

public class MyReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private MediaPlayer mPlayer = null;
    private Context myContext;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String expediteur;

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
                expediteur = smsMessage.getOriginatingAddress();

                traiteMessage(smsBody);
            }

        }

    }

    private void traiteMessage(String smsBody) {

        if (smsBody.startsWith(MainActivity.CODE_RING)) {
            Intent intent = new Intent(myContext, AlertAlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);

        } else if (smsBody.startsWith(MainActivity.CODE_TEXT)) {
            Intent intent2 = new Intent(myContext, AlertAlarmActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent2);

        } else if (smsBody.startsWith(MainActivity.CODE_VOCAL)) {
            // TODO lancer Activité Vocal
        } else if (smsBody.startsWith(MainActivity.CODE_LOCALISER)) {
            locationManager = (LocationManager) myContext.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    returnLocation(location);
                    removeUpdates();
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        } else if (smsBody.startsWith("RETURN_LOCATION")) {
            Intent intent4 = new Intent(myContext, MapsActivity.class);
            intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent4);
        }
    }

    private void removeUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    private void returnLocation(Location location) {
        SmsManager smsManager = SmsManager.getDefault();
        String contenu = "RETURN_LOCATION ";
        contenu += location.getLongitude();
        contenu += ", " + location.getLatitude();
        smsManager.sendTextMessage(expediteur, null, contenu, null, null);
    }


}
