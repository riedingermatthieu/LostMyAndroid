package fr.enac.lostmyandroid.views;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import fr.enac.lostmyandroid.controllers.MainActivityController;
import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.utilities.PopupMessage;

/**
 * Activity principale de l'application. Elle présente les 4 différentes fonctionnalités
 * que l'utilisateur peut lancer : <i>Sonner, Envoyer un message vocal, Envoyer un message
 * texte, Localiser et afficher sur un Map.</i>
 */
public class MainActivity extends AppCompatActivity implements PopupMessage.NoticeDialogListener {

    private MainActivityController myController;
    public static String vocalMessageLocation;

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
     * Implémentation de l'interface de dialogue (Pop Up Message)
     *
     * @param dialog le fragment de dialogue qui s'affichera
     * @see class PopUpMessage
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
        if (id == R.id.record_vocal) {
            int ACTIVITY_RECORD_SOUND = 1;
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //Bundle extras = data.getExtras();
            try {
                Uri u = data.getData();
                try {
                    vocalMessageLocation = getRealPathFromURI(u);
                } catch (Exception ex) {
                    vocalMessageLocation = u.getPath();
                }
            } catch (Exception ex) {
                String s = ex.toString();
            }
        } else {
            Log.i("vocal record", "onActivityResult Failed to get music");
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // FIXME le faire pour l'activity de l'alarme
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Back pressed in main", Toast.LENGTH_LONG).show();
    }


}
