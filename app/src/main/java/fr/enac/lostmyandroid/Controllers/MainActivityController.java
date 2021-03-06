package fr.enac.lostmyandroid.Controllers;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.utilities.MyReceiver;
import fr.enac.lostmyandroid.utilities.PopupMessage;
import fr.enac.lostmyandroid.utilities.VibrationSensor;

/**
 * Contrôleur de la vue principale de l'application
 */
public class MainActivityController {

    // Receiver Codes
    public static final String CODE_RING = "RING";
    public static final String CODE_TEXT = "TEXT";
    public static final String CODE_VOCAL = "VOCAL";
    public static final String CODE_LOCALISER = "LOCATION";

    // Main Activity
    private Activity myActivity;

    //View Data
    private EditText number;
    private Button ringIt;
    private Button vocalMessage;
    private Switch antiArrachement;
    private Button textMessage;
    private Button localiser;
    private PopupMessage pm;


    // Managers
    private SmsManager smsManager;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorListener;



    /**
     * Constructeur
     * @param main L'activité qu'il contrôle : MainActivity
     */
    public MainActivityController(Activity main) {
        myActivity = main;
        smsManager = SmsManager.getDefault();
        number = (EditText) myActivity.findViewById(R.id.editNumero);
        ringIt = (Button) myActivity.findViewById(R.id.buttonRing);
        vocalMessage = (Button) myActivity.findViewById(R.id.buttonVocalMessage);
        textMessage = (Button) myActivity.findViewById(R.id.buttonTextMessage);
        antiArrachement = (Switch) myActivity.findViewById(R.id.switchArrachement);
        localiser = (Button) myActivity.findViewById(R.id.buttonLocaliser);
    }


    /* L'enregistrement des callbacks */

    /**
     * Enregistre le callback sur le bouton faire sonner
     */
    public void setRingListener() {
        ringIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide())
                {
                    ring(number.getText().toString());

                    // FIXME for tests on tablet purpose
                    // MyReceiver.startRingAlarm(myActivity.getApplicationContext());

                }
            }
        });
    }

    /**
     * Enregistre le callback sur le message vocal
     */
    public void setVocalListener() {
        vocalMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide())
                    vocal(number.getText().toString());
            }
        });
    }

    /**
     * Enregistre le callback sur le switch antivol
     */
    public void setOncheckSwitchListener() {
        antiArrachement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    initSensor();
                else
                    abortSensor();
            }
        });
    }
    /**
     * Enregistre le callback sur le bouton Localiser
     */
    public void setLocaliserListener() {
        localiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide())
                    localize(number.getText().toString());
            }
        });

    }

    /**
     * Enregistre le callback sur le bouton envoyer message
     */
    public void setTextMessageListener() {
        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide()) {
                    pm = new PopupMessage();
                    pm.show(myActivity.getFragmentManager(), "");
                }
            }
        });
    }


    /* Les fonctionnalités */

    /**
     * Envoyer un message avec code RING
     * @param numero Le destinataire
     */
    public void ring(String numero) {
        smsManager.sendTextMessage(numero, null, CODE_RING, null, null);
    }

    /**
     * Envoyer un message avec code VOCAL
     * @param numero Le destinataire
     */
    public void vocal(String numero) {
        smsManager.sendTextMessage(numero, null, CODE_VOCAL, null, null);
    }

    /**
     * Envoyer un message avec code LOCATION
     * @param numero Le destinataire
     */
    public void localize(String numero) {
        smsManager.sendTextMessage(numero, null, CODE_LOCALISER, null, null);
    }

    /**
     * Envoyer un message avec code TEXT
     * @param numero Le destinataire
     */
    public void message(String numero, String contenuMessage) {
        smsManager.sendTextMessage(numero, null, CODE_TEXT + ": " + contenuMessage, null, null);
        Log.e("ERROR", "message sent");
    }


    /* Utilitaires */

    /**
     * Test si le numéro mesure 10 chiffres
     * @return boolean Vrai ou Faux
     */
    private boolean numeroValide() {
        String numero = number.getText().toString();
        if (numero.length() == 10)
            return true;
        else {
            Toast.makeText(myActivity.getApplicationContext(), "Le numéro entré n'est pas valide", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Initialise le sensors accéléromètres
     */
    public void initSensor() {
        mSensorListener = new VibrationSensor(myActivity.getApplicationContext());

        mSensorManager = (SensorManager) myActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Désenregistrer les sensors pour ne pas perdre de batterie
     */
    public void abortSensor() {
        mSensorManager.unregisterListener(mSensorListener);
    }
}
