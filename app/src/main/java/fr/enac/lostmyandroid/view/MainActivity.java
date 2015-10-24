package fr.enac.lostmyandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.SortedMap;

import fr.enac.lostmyandroid.R;
import fr.enac.lostmyandroid.utilities.PopupMessage;

public class MainActivity extends AppCompatActivity {
    private EditText number;
    private Button ringIt;
    private Button vocalMessage;
    private Button textMessage;

    private Switch numero;

    private SmsManager smsManager;

    public static final String CODE_RING = "RING";
    public static final String CODE_TEXT = "TEXT";
    public static final String CODE_VOCAL = "VOCAL";

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

        //
        ringIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numeroValide()) {
                    smsManager.sendTextMessage(getNumero(), null, CODE_RING, null, null);
                }
                else
                    Toast.makeText(getApplicationContext(), "Le numéro entré n'est pas valide", Toast.LENGTH_SHORT).show();
            }
        });

        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide()) {
                    PopupMessage pm = new PopupMessage();
                    pm.setNumero(getNumero());
                    pm.show(getFragmentManager(), "");
                }
                else
                    Toast.makeText(getApplicationContext(), "Le numéro entré n'est pas valide", Toast.LENGTH_SHORT).show();
            }
        });

        vocalMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numeroValide()) {
                    smsManager.sendTextMessage(getNumero(), null, CODE_VOCAL, null, null);
                }
                else
                    Toast.makeText(getApplicationContext(), "Le numéro entré n'est pas valide", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean numeroValide() {
        String numero = number.getText().toString();
        return numero.length() == 10;
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
}
