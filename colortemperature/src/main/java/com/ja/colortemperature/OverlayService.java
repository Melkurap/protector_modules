package com.ja.colortemperature;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

public class OverlayService extends Service {

    private OverlayView view;
    private final ColorCalculations color = new ColorCalculations();
    private int intensity = 0;

    private static final int ONGOING_NOTIFICATION = 1;

    private int orientation;


    public OverlayService() {
        view = new OverlayView(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
          view = new OverlayView(this);

        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, ColorTemperatureActivity.class), 0);
        Notification.Builder notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Barwa temperaturowa")
                .setContentText("Kliknij, aby przejść do ustawień.")
                .setContentIntent(pi);
               // .getNotification();

        startForeground(ONGOING_NOTIFICATION, notification.build());

        orientation = getResources().getConfiguration().orientation;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.detach(getWindowManager());
      //  running = false;
      //  Log.i(TAG, "#onDestroy()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //running = true;

        intensity = intent.getIntExtra("Intensity",InitialValues.INTENSITY);
        int colorTemperature = intent.getIntExtra("Color temperature", InitialValues.COLOR_TEMPERATURE);
        int dimness = intent.getIntExtra("Dimness", InitialValues.DIMNESS);

        color.setDarkness(dimness).setColorTemperature(colorTemperature);

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

      //  Log.i(TAG, "#onStartCommand() values: " + intensity + "," + colorTemperature + "," + dimness);

        view.setColor(intensity, r, g, b).attach(getWindowManager());

        return super.onStartCommand(intent, flags, startId);
    }

    private WindowManager getWindowManager() {
        return (WindowManager) getSystemService(WINDOW_SERVICE);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
