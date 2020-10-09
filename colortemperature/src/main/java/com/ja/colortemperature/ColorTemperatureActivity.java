package com.ja.colortemperature;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class ColorTemperatureActivity extends AppCompatActivity implements SensorEventListener {

    TextView textView;
    TextView description;
    Switch serviceSwitch;
    Button button;
    Button buttonExit;
    Boolean sensorRunning;

    private SensorManager sensorManager;
    private Sensor light;

    public int intensity = InitialValues.INTENSITY;
    public int colorTemperature = InitialValues.COLOR_TEMPERATURE;
    public int dimness = InitialValues.DIMNESS;
    public int rangeColorTemp = InitialValues.RANGE_COLOR_TEMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_temperature);

        textView = findViewById(R.id.textView);
        description = findViewById(R.id.textView2);
        serviceSwitch = findViewById(R.id.switch1);
        button = findViewById(R.id.button);
        buttonExit = findViewById(R.id.button_exit);


        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {
                    startFilterService();
                }
                else{

                    stopFilterService();
                }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( sensorRunning == true ){
                button.setText(R.string.sensor_on);
                    onPause();
                }else {
                    button.setText(R.string.sensor_off);
                    onResume();
                }

            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(light.TYPE_LIGHT);

        final SeekBar colorTempSeekBar =
                findViewById(R.id.colorTemperatureSeekBar);
        final SeekBar opacitySeekBar =  findViewById(R.id.opacitySeekBar);
        final SeekBar darknessSeekBar = findViewById(R.id.darknessSeekBar);

        //       load();

        colorTempSeekBar.setProgress(colorTemperature);
        opacitySeekBar.setProgress(intensity);
        darknessSeekBar.setProgress(dimness);

        colorTempSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerExtended() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorTemperature = progress;
                startFilterService();
            }
        });

        opacitySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerExtended() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensity = progress;
                startFilterService();

            }
        });


        darknessSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerExtended() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dimness = progress;
                startFilterService();
            }
        });

    }

    public void startFilterService() {                   // umożliwia uruchomienie w tle po wcisnięciu strzałki ablo automatycznie
        Intent intent = new Intent(this, OverlayService.class);

        intent.putExtra("Color temperature", colorTemperature * rangeColorTemp);
        intent.putExtra("Intensity", intensity);
        intent.putExtra("Dimness", dimness);

        this.startService(intent);

        serviceSwitch.setChecked(true);

    }
    public void stopFilterService() {
        Intent intent = new Intent(this, OverlayService.class);

        this.stopService(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT);
        textView.setText(event.values[0] + " lx"  );

        if( event.values[0] <= 100) {
           description.setText("Słabe światło lub brak we wnętrzu pomieszczenia");

           InitialValues.COLOR_TEMPERATURE = 15;
           InitialValues.INTENSITY = 160;
           InitialValues.DIMNESS = 75;

           serviceSwitch.setChecked(true);
           sensorLightService();


        } else if (event.values[0] >= 101  && event.values[0] <= 1000 ){

            description.setText("Światło dzienne w mieszkaniu");

            InitialValues.COLOR_TEMPERATURE = 40;
            InitialValues.INTENSITY = 130;
            InitialValues.DIMNESS = 75;

            serviceSwitch.setChecked(true);
            sensorLightService();


        }else if (event.values[0] >= 1001  && event.values[0] <= 10000 ) {

            description.setText("Pochmurny dzień na zewnątrz");

            InitialValues.COLOR_TEMPERATURE = 75;
            InitialValues.INTENSITY = 70;
            InitialValues.DIMNESS = 25;

            serviceSwitch.setChecked(true);
            sensorLightService();

        } else if (event.values[0] >= 10001 ){
             description.setText("Pogodny dzień na zewnątrz");

            InitialValues.COLOR_TEMPERATURE = 100;
            InitialValues.INTENSITY = 70;
            InitialValues.DIMNESS = 0;

            serviceSwitch.setChecked(true);
            sensorLightService();

        }

    }

    public void sensorLightService(){

        Intent intent = new Intent(this, OverlayService.class);

        intent.putExtra("Color temperature", InitialValues.COLOR_TEMPERATURE * rangeColorTemp);
        intent.putExtra("Intensity", InitialValues.INTENSITY);
        intent.putExtra("Dimness", InitialValues.DIMNESS);

        this.startService(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);

        sensorRunning = true;
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);

        sensorRunning = false;
    }

    public abstract class OnSeekBarChangeListenerExtended implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
