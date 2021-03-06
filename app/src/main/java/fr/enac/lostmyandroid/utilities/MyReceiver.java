package fr.enac.lostmyandroid.utilities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import fr.enac.lostmyandroid.controllers.MainActivityController;
import fr.enac.lostmyandroid.views.AlertAlarmActivity;
import fr.enac.lostmyandroid.views.MapsActivity;
import fr.enac.lostmyandroid.views.VocalMessageActivity;

public class MyReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private MediaPlayer mPlayer = null;
    private Context myContext;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String expediteur;

    private final String RETURN_LOCATION = "RETURN_LOCATION";

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

        if (smsBody.startsWith(MainActivityController.CODE_RING))
        {
            Intent intent = new Intent(myContext, AlertAlarmActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);

        }
        else if (smsBody.startsWith(MainActivityController.CODE_TEXT))
        {
            Intent intent2 = new Intent(myContext, AlertAlarmActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String message = smsBody.substring(MainActivityController.CODE_TEXT.length()+2);
            intent2.putExtra("message", message);
            myContext.startActivity(intent2);

        }
        else if (smsBody.startsWith(MainActivityController.CODE_VOCAL))
        {
            Intent intent = new Intent(myContext, VocalMessageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myContext.startActivity(intent);
        }
        else if (smsBody.startsWith(MainActivityController.CODE_LOCALISER))
        {
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

        }
        else if (smsBody.startsWith(RETURN_LOCATION))
        {
            Intent intent4 = new Intent(myContext, MapsActivity.class);
            intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String result = smsBody.substring(RETURN_LOCATION.length());
            String location[] = result.split(",");

            double longitude = Double.valueOf(location[0]);
            double latitude = Double.valueOf(location[1]);

            Log.d("LatLng", "Avant: " + longitude);
            Log.d("LatLng", "Avant: " + latitude);

            intent4.putExtra("longitude", longitude);
            intent4.putExtra("latitude", latitude);
            myContext.startActivity(intent4);
        }
    }

    // For test purpose on tablet that doesn't have sms abilities
    public static void startRingAlarm(Context cont) {
        Intent intent = new Intent(cont, AlertAlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        cont.startActivity(intent);
    }

    private void removeUpdates() {
        locationManager.removeUpdates(locationListener);
    }

    private void returnLocation(Location location) {
        SmsManager smsManager = SmsManager.getDefault();
        String contenu = "";
        contenu += RETURN_LOCATION;
        contenu += location.getLongitude();
        contenu += "," + location.getLatitude();
        smsManager.sendTextMessage(expediteur, null, contenu, null, null);
    }
}
