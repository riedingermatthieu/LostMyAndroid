package fr.enac.lostmyandroid.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.enac.lostmyandroid.Controllers.AlertAlarmActivityController;
import fr.enac.lostmyandroid.R;

public class AlertAlarmActivity extends AppCompatActivity {

    private Integer lockCode; // Pour le code de sécurité TODO à revoir
    //private MediaPlayer mPlayer = null;
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



}
