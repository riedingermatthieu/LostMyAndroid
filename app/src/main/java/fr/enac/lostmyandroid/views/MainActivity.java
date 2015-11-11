package fr.enac.lostmyandroid.views;

import android.app.DialogFragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import fr.enac.lostmyandroid.Controllers.MainActivityController;
import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.utilities.MyReceiver;
import fr.enac.lostmyandroid.utilities.PopupMessage;
import fr.enac.lostmyandroid.utilities.VibrationSensor;

/**
 * Activity principale de l'application. Elle présente les 4 différentes fonctionnalités
 * que l'utilisateur peut lancer : <i>Sonner, Envoyer un message vocal, Envoyer un message
 * texte, Localiser et afficher sur un Map.</i>
 */
public class MainActivity extends AppCompatActivity implements PopupMessage.NoticeDialogListener{

    private MainActivityController myController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciation Controlleur
        myController = new MainActivityController(this);

        // Enregistrement des Callbacks
        myController.setRingListener();
        myController.setVocalListener();
        myController.setOncheckSwitchListener();
        myController.setLocaliserListener();
        myController.setTextMessageListener();

    }

    /**
     *  Implémentation de l'interface de dialogue (Pop Up Message)
     *  @param dialog le fragment de dialogue qui s'affichera
     *  @see class PopUpMessage
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        EditText message = (EditText) dialog.getDialog().findViewById(R.id.message);
        String contenuMessage = message.getText().toString();
        EditText number = (EditText) findViewById(R.id.editNumero);

        myController.message(number.getText().toString(), contenuMessage);

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

    // FIXME le faire pour l'activity de l'alarme
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(),"Back pressed in main", Toast.LENGTH_LONG).show();
    }


}
