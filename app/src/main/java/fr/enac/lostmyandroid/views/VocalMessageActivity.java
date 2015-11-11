package fr.enac.lostmyandroid.views;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fr.enac.lostmyandroid.R;

public class VocalMessageActivity extends AppCompatActivity {

    private static final int ADMIN_INTENT = 15;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocal_message);

        mp = new MediaPlayer();

        if (MainActivity.vocalMessageLocation != null)
            audioPlayer(MainActivity.vocalMessageLocation);

        Button buttonStop = (Button) findViewById(R.id.stop_vocal);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void audioPlayer(String path) {
        //set up MediaPlayer
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
