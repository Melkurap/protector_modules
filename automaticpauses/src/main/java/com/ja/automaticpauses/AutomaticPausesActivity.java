package com.ja.automaticpauses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class AutomaticPausesActivity extends AppCompatActivity {

    TextView microTimertext;
    TextView macroTimertext;

    private CountDownTimer microCountDownTimer;
    private CountDownTimer macroCountDownTimer;

    private EditText setMicroPause;
    private EditText setMacroPause;
    private Button buttonSet;
    private Button buttonStartPause;
    private Button buttonReset;
    private Button buttonExit;

    Switch serviceSwitch;

    private boolean isTimerRunning;

    protected long startMicroPauseTime = 18000;
    protected long startMacroPauseTime = 180000;

    protected long leftMicroPauseTime = startMicroPauseTime;
    protected long leftMacroPauseTime = startMacroPauseTime;

    private long endMicroPauseTime;
    private long endMacroPauseTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_pauses);
        this.setFinishOnTouchOutside(false); // stops activity when user clicks outside activity



        setMicroPause = findViewById(R.id.editTextImput);
        setMacroPause = findViewById(R.id.editTextImput2);

        buttonSet = findViewById(R.id.Ustaw);
        buttonReset = findViewById(R.id.button_reset);
        buttonStartPause = findViewById(R.id.button_start_pause);
        buttonExit = findViewById(R.id.button_exit);

        serviceSwitch = findViewById(R.id.switch1);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });



        buttonStartPause.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isTimerRunning){
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true)
                {
                    startService();
                }
                else{

                    stopService();
                }

            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonReset.performClick();
                buttonStartPause.performClick();
                //buttonStartService.performClick();

            }
        }, 0);

        updateCountDownTimerTekst();

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputMicroPause = setMicroPause.getText().toString();
                if (inputMicroPause.length() == 0) {
                    Toast.makeText(AutomaticPausesActivity.this, "Pole nie może być puste", Toast.LENGTH_SHORT).show();
                    return;
                }

                String inputMacroPause = setMacroPause.getText().toString();
                if (inputMacroPause.length() == 0) {
                    Toast.makeText(AutomaticPausesActivity.this, "Pole nie może być puste", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInputMicroPause = Long.parseLong(inputMicroPause) * 60000;
                if (millisInputMicroPause == 0) {
                    Toast.makeText(AutomaticPausesActivity.this, "Proszę wpisać wartość dodatnią", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInputMacroPause = Long.parseLong(inputMacroPause) * 60000;
                if (millisInputMacroPause == 0) {
                    Toast.makeText(AutomaticPausesActivity.this, "Proszę wpisać wartość dodatnią", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInputMicroPause, millisInputMacroPause);
                setMicroPause.setText("");
                setMacroPause.setText("");
            }
        });


    }

    private void setTime(long milliseconds, long milliseconds2){
        startMicroPauseTime = milliseconds;
        startMacroPauseTime = milliseconds2;
        resetTimer();
        microCountDownTimer.cancel();  //  cancel current countdown timer post setting timer
        macroCountDownTimer.cancel();  //
        leftMacroPauseTime = startMacroPauseTime;
        startTimer();                           // well :)
        closeKeyboard();
    }


    private void startTimer() {

        endMicroPauseTime = System.currentTimeMillis() + leftMicroPauseTime;
        endMacroPauseTime = System.currentTimeMillis() + leftMacroPauseTime;

        microCountDownTimer = new CountDownTimer(leftMicroPauseTime, 1000) {

            public void onTick(long millisUntilFinished) {

                leftMicroPauseTime = millisUntilFinished;
                updateCountDownTimerTekst();

            }

            public void onFinish() {

                isTimerRunning = false;
                onStop();
                pauseTimer();  // it's important to stop macropause, and avoid multiple "windows" activity
                Intent intent = new Intent(AutomaticPausesActivity.this, MicroPauseEventActivity.class);
                startActivity(intent);
                finish();

            }


        }.start();


        macroCountDownTimer = new CountDownTimer(leftMacroPauseTime, 1000) {

            public void onTick(long millisUntilFinished) {

                leftMacroPauseTime = millisUntilFinished;
                updateCountDownTimerTekst();

               }

            public void onFinish() {
                isTimerRunning = false;
                leftMacroPauseTime = startMacroPauseTime; //START_MACRO_TIMER_IN_MILLIS;
                onStop();
                pauseTimer(); // it's important to avoid bugs bound up with macropause

                Intent intent = new Intent(AutomaticPausesActivity.this, MacroPauseEventActivity.class);
                startActivity(intent);
                finish();

            }
        }.start();

    }

    private void  pauseTimer(){

        microCountDownTimer.cancel();
        macroCountDownTimer.cancel();
        isTimerRunning = false;

    }



    private void resetTimer(){

        leftMicroPauseTime = startMicroPauseTime;
  //    leftMacroPauseTime  = startMacroPauseTime;
        updateCountDownTimerTekst();

    }


    private void updateCountDownTimerTekst(){

        microTimertext = (TextView) findViewById(R.id.textView1);
        macroTimertext = (TextView) findViewById(R.id.textView5);

        microTimertext.setText("Do mikroprzerwy pozostało: " + leftMicroPauseTime / 60000 + ":" + String.format("%02d", ((leftMicroPauseTime / 1000) % 60)));
        macroTimertext.setText("Do makroprzerwy pozostało: " + leftMacroPauseTime / 60000 + ":" + String.format("%02d", ((leftMacroPauseTime / 1000) % 60)));

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

          SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE );
          SharedPreferences.Editor editor = sp.edit();

        editor.putLong("startMicroPauseTime", startMicroPauseTime);
        editor.putLong("startMacroPauseTime", startMacroPauseTime);
        editor.putLong("leftMicroPauseTime", leftMicroPauseTime);
        editor.putLong("leftMacroPauseTime", leftMacroPauseTime);
        editor.putBoolean("isTimerRunning", isTimerRunning);
        editor.putLong("endMicroPauseTime", endMicroPauseTime);
        editor.putLong("endMacroPauseTime", endMacroPauseTime);

        editor.apply();

    }

   @Override
    protected void onStart() {
        super.onStart();

       SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);

       startMicroPauseTime = sp.getLong("startMicroPauseTime", startMicroPauseTime); // instead of specific value, it's attributed variable(which should be set up earlier). On the whole I doubt if this solve is correct
       startMacroPauseTime = sp.getLong("startMacroPauseTime", startMacroPauseTime);

       leftMicroPauseTime = sp.getLong("leftMicroPauseTime", startMicroPauseTime);//START_MICRO_TIMER_IN_MILLIS
       leftMacroPauseTime = sp.getLong("leftMacroPauseTime", startMacroPauseTime);//START_MACRO_TIMER_IN_MILLIS

        isTimerRunning = sp.getBoolean("timerRunning", false);

        updateCountDownTimerTekst();
        //update buttons?
        if(isTimerRunning){
            endMicroPauseTime = sp.getLong("endMicroPauseTime",0);
            endMacroPauseTime = sp.getLong("endMacroPauseTime", 0);

            leftMicroPauseTime = endMicroPauseTime - System.currentTimeMillis();
            leftMacroPauseTime = endMacroPauseTime - System.currentTimeMillis();

            if ( leftMicroPauseTime < 0 ){
                leftMicroPauseTime = 0;
                isTimerRunning = false;
                updateCountDownTimerTekst();

            }
            else if (leftMacroPauseTime < 0){
                leftMacroPauseTime = 0;
                isTimerRunning = false;
                updateCountDownTimerTekst();
            }
            else { startTimer();
            }
        }
    }


    public void startService(){

        String input = microTimertext.getText().toString();
        String input2 = macroTimertext.getText().toString();

        Intent serviceIntent = new Intent(this, AutomaticPausesService.class);
        serviceIntent.putExtra("inputExtra", input);
        serviceIntent.putExtra("inputExtra2", input2);

        ContextCompat.startForegroundService(this, serviceIntent);

    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, AutomaticPausesService.class);
        stopService(serviceIntent);
    }

    protected void onResume(){

        super.onResume();
        serviceSwitch.setChecked(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}