package fr.enac.lostmyandroid.view;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.SortedMap;

import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.utilities.MyReceiver;
import fr.enac.lostmyandroid.utilities.PopupMessage;

public class MainActivity extends AppCompatActivity implements PopupMessage.NoticeDialogListener{

    //View Data
    private EditText number;
    private Button ringIt;
    private Button vocalMessage;
    private Switch antiArrachement;
    private Button textMessage;
    private Button localiser;

    private Switch numero;

    public static final String CODE_RING = "RING";
    public static final String CODE_TEXT = "TEXT";
    public static final String CODE_VOCAL = "VOCAL";
    public static final String CODE_LOCALISER = "LOCATION";

    private SmsManager smsManager;

    private EditText message;
    private PopupMessage pm;


    // Shake detection Data
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        boolean alarmActivated = false;

        public void onSensorChanged(SensorEvent se) {

            if(!alarmActivated) {
                float x = se.values[0];
                float y = se.values[1];
                float z = se.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta; // perform low-cut filter

                if (mAccel > 1) {
                    alarmActivated = true;
                    Intent intent = new Intent(getApplicationContext(), AlertAlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }
    public void abortSensor() {
        mSensorManager.unregisterListener(mSensorListener);
    }


    // Pop-up Send message
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        message = (EditText) dialog.getDialog().findViewById(R.id.message);
        String contenuMessage = message.getText().toString();


        Log.d("Res", contenuMessage);

        smsManager.sendTextMessage(getNumero(), null, CODE_TEXT + ": " + contenuMessage, null, null);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsManager = SmsManager.getDefault();

        // Loading components
        number = (EditText) findViewById(R.id.editNumero);
        ringIt = (Button) findViewById(R.id.buttonRing);
        vocalMessage = (Button) findViewById(R.id.buttonVocalMessage);
        textMessage = (Button) findViewById(R.id.buttonTextMessage);
        antiArrachement = (Switch) findViewById(R.id.switchArrachement);
        localiser = (Button) findViewById(R.id.buttonLocaliser);

        ringIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide())
                {
                    smsManager.sendTextMessage(getNumero(), null, CODE_RING, null, null);

                    // FIXME for tests on tablet purpose
                    MyReceiver.startRingAlarm(getApplicationContext());
                    //finish();
                }
            }
        });

        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide()) {
                    pm = new PopupMessage();
                    pm.show(getFragmentManager(), "");
                }
            }
        });

        vocalMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide()) {
                    smsManager.sendTextMessage(getNumero(), null, CODE_VOCAL, null, null);
                }
            }
        });

        antiArrachement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    initSensor();
                else
                    abortSensor();
            }
        });

        localiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide())
                    smsManager.sendTextMessage(getNumero(), null, CODE_LOCALISER, null, null);
            }
        });


    }


    private boolean numeroValide() {
        String numero = number.getText().toString();
        if (numero.length() == 10)
            return true;
        else {
            Toast.makeText(getApplicationContext(), "Le numéro entré n'est pas valide", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String getNumero() {
        return number.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Back pressed in main", Toast.LENGTH_LONG).show();

    }




}
