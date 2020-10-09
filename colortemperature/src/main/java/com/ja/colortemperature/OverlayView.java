package com.ja.colortemperature;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class OverlayView extends View {

    private boolean attached;
    private int a;
    private int r;
    private int b;
    private int g;

    public OverlayView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(this.a, this.r, this.b, this.g);
    }
    public OverlayView setColor(int a, int r, int b, int g) {
        this.a = a;
        this.r = r;
        this.b = b;
        this.g = g;

        this.invalidate();
        return this;
    }
    public boolean isAttached() {
        return this.attached;
    }

    public OverlayView attach(WindowManager wm) {      // enable setting up the overlay
        if (this.isAttached()) return this;

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(size.x, size.y + 200, 0, 0,
                WindowManager.LayoutParams.TYPE_TOAST, flags, PixelFormat.TRANSPARENT); //  TYPE_TOAST is needed to overlay work on API <= 23

        this.attached = true;
        wm.addView(this, layoutParams);
        return this;
    }

    public OverlayView detach(WindowManager wm) {       // enable taking down the overlay
        wm.removeView(this);
        this.attached = false;
        return this;
    }
}
