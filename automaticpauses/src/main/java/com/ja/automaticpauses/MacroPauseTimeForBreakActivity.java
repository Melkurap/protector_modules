package com.ja.automaticpauses;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

public class MacroPauseTimeForBreakActivity extends AppCompatActivity {

    TextView breakTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_macro_pause_time_for_break);

        breakTime =(TextView)findViewById(R.id.textView1);

        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {

                breakTime.setText("Do końca makroprzerwy pozostało: " + millisUntilFinished /60000  +":"+String.format("%02d",((millisUntilFinished / 1000) % 60)));


            }

            public void onFinish() {

                Intent intent = new Intent(MacroPauseTimeForBreakActivity.this, AutomaticPausesActivity.class );
                startActivity(intent);
                finish();
                Intent BroadcastIntent = new Intent("finish_activity");
                sendBroadcast(BroadcastIntent);

            }


        }.start();


    }
}
