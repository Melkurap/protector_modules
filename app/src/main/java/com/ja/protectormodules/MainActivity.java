package com.ja.protectormodules;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {


    Button buttonAutomaticPauses;
    Button buttonColorTemperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonAutomaticPauses = findViewById(R.id.buttonAutomaticPauses);
        buttonColorTemperature = findViewById(R.id.buttonColorTemperature);

        buttonAutomaticPauses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ja.automaticpauses");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }

            }
        });

        buttonColorTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ja.colortemperature");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                }
            }
        });


    }
}
