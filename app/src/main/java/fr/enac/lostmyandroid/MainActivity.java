package fr.enac.lostmyandroid;

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

import fr.enac.lostmyandroid.utilities.PopupMessage;

public class MainActivity extends AppCompatActivity {
    private EditText number;
    private Button ringIt;
    private Button vocalMessage;
    private Button textMessage;

    private Switch numero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loading components
        number = (EditText) findViewById(R.id.editNumero);
        ringIt = (Button) findViewById(R.id.buttonRing);
        vocalMessage = (Button) findViewById(R.id.buttonVocalMessage);
        textMessage = (Button) findViewById(R.id.buttonTextMessage);

        //
        ringIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number.getText().toString(), null, "RING", null, null);
            }
        });

        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMessage pm = new PopupMessage();

                pm.show(getFragmentManager(), "");
            }
        });

        vocalMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number.getText().toString(), null, "VOCAL", null, null);
            }
        });

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
