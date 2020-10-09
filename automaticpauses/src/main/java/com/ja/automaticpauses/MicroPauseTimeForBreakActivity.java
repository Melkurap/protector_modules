package com.ja.automaticpauses;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

public class MicroPauseTimeForBreakActivity extends AppCompatActivity {

    TextView breakTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_micro_pause_time_for_break);

        breakTime =(TextView)findViewById(R.id.textView1);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                breakTime.setText("Do końca mikroprzerwy pozostało: " + millisUntilFinished /60000  +":"+String.format("%02d",((millisUntilFinished / 1000) % 60)));

            }

            public void onFinish() {

                Intent intent = new Intent(MicroPauseTimeForBreakActivity.this, AutomaticPausesActivity.class );
                startActivity(intent);
                finish();
                Intent BroadcastIntent = new Intent("finish_activity");
                sendBroadcast(BroadcastIntent);


            }


        }.start();



    }
}
