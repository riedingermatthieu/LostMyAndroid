package fr.enac.lostmyandroid.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import fr.enac.lostmyandroid.Controllers.AlertAlarmActivityController;
import fr.enac.lostmyandroid.R;

public class AlertAlarmActivity extends AppCompatActivity {

    private AlertAlarmActivityController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_alarm);

        String message = getIntent().getStringExtra("message");
        myController = new AlertAlarmActivityController(this, message);

        myController.putMaxSound();
        myController.playSound(R.raw.alarm);
        myController.showMessage();

    }

    @Override
    protected void onStop() {
        super.onStop();
        myController.stopSound();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AlertAlarmActivityController.ADMIN_INTENT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
