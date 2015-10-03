package fr.enac.lostmyandroid;



import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // TODO Implement SMS Receiving behaviour

        }
    }
}
