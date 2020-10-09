package com.ja.automaticpauses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MicroPauseEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_micro_pause_event);


        final Button buttonYes = findViewById(R.id.button3);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MicroPauseEventActivity.this, MicroPauseTimeForBreakActivity.class );
                startActivity(intent);
                finish();

            }
        });

        final Button buttonNo = findViewById(R.id.button4);
        buttonNo.setOnClickListener(new View.OnClickListener() {
                                      public void onClick(View v) {
                                          Intent intent = new Intent(MicroPauseEventActivity.this, AutomaticPausesActivity.class );
                                          startActivity(intent);
                                          finish(); // Code here executes on main thread after user presses button
                                      }
                                  });




    }
}

