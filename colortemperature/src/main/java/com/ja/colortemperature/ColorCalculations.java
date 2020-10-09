package com.ja.colortemperature;


public class ColorCalculations {

    private float colorTemperature;
    private int darkness;

    public ColorCalculations setColorTemperature(float colorTemperature) {
        if (colorTemperature < 1000) colorTemperature = 1000;
        if (colorTemperature > 40000) colorTemperature = 40000;

        this.colorTemperature = colorTemperature;
        return this;
    }

    public ColorCalculations setDarkness(int darkness) {
        this.darkness = darkness;
        return this;
    }

    public float getColorTemperature() {
        return colorTemperature;
    }

    public int getDarkness() {
        return darkness;
    }

    private int scope(int num) {
        return num < 0 ? 0 : num > 255 ? 255: num;
    }

    protected int getCalculatedRed() {
        float temperature = this.colorTemperature / 100;
        if (temperature <= 66) return 255;

        int redComponent = (int) (329.698727446 * Math.pow(temperature - 60, -0.1332047592));
        return scope(redComponent);
    }

    protected int getCalculatedGreen() {
        int greenComponent;
        float temperature = this.colorTemperature / 100;
        if (temperature <= 66) {
            greenComponent = (int) (99.4708025861 * Math.log(temperature) - 161.1195681661);
        }
        else {
            greenComponent =  (int) (288.1221695283 * Math.pow(temperature - 60, -0.0755148492));
        }

        return scope(greenComponent);
    }
    protected int getCalculatedBlue() {
        float temperature = this.colorTemperature / 100;
        if (temperature >= 66) return 255;
        if (temperature <= 19) return 0;
        int blueComponent = (int) (138.5177312231 * Math.log(temperature - 10) - 305.0447927307);
        return scope(blueComponent);
    }
    protected int calculateDimTint(int color) {
        float factor = 1 - ((float) darkness) / 100;
        return (int) (factor * color);
    }

    public int getRed() {
        int red = getCalculatedRed();
        return calculateDimTint(red);
    }

    public int getGreen() {
        int green = getCalculatedGreen();
        return calculateDimTint(green);
    }

    public int getBlue() {
        int blue = getCalculatedBlue();
        return calculateDimTint(blue);
    }
}
