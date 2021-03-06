package pl.slapps.dot.drawing;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by piotr on 14/02/16.
 */
public class Util {

    private static String TAG = Util.class.getName();

    public static float[] parseColor(String color) {

        float[] returnValue = new float[]{0, 0, 0, 0};
        try {
            int intColor = Color.parseColor(color);

            float a = (float) Color.alpha(intColor) / 255;
            float r = (float) Color.red(intColor) / 255;
            float g = (float) Color.green(intColor) / 255;
            float b = (float) Color.blue(intColor) / 255;
            //Log.d(TAG, "color setted " + color);
            returnValue = new float[]{r, g, b, a};
        } catch (Throwable t) {
        }
        return returnValue;
    }

    public static boolean isColorDark(String stringColor) {
        int color = Color.parseColor(stringColor);
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }

    public static float[] summArrays(float[] array, float[] array_two)
    {
        float[] output = new float[array.length+array_two.length];
        for(int i=0;i<array.length;i++)
        {
            output[i]=array[i];
        }
        for(int i=0;i<array_two.length;i++)
        {
            output[array.length+i]=array_two[i];
        }

        return output;
    }

    public static String changeColorBrightness(String stringColor, boolean light) {
        int color = Color.parseColor(stringColor);
        float r = (float) Color.red(color) / 255;
        float g = (float) Color.green(color) / 255;
        float b = (float) Color.blue(color) / 255;

        float multiplier = light ? 1.5f : 0.5f;

        r = r * multiplier;
        g = g * multiplier;
        b = b * multiplier;

        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;

        color = Color.rgb((int) r, (int) g, (int) b);

        return "#" + Integer.toHexString(color);

    }


    public static String getRainbowByPosition(int position, int max) {

        if(position==0)
            position=1;

        int degree = (int) (360.0f * (float) position / (float) max);


        int r = (int)(Math.sin(degree) * 127 + 128);
        int g = (int)(Math.sin(degree) * 127 + 128);
        int b = (int)(Math.sin(degree) * 127 + 128);

        int color = Color.rgb(r, g, b);

        return "#" + Integer.toHexString(color);

    }


    public static String calculateColorsSwitch(String colorStart, String colorEnd, float progress) {
        int startColor = Color.parseColor(colorStart);
        int endColor = Color.parseColor(colorEnd);

        if (progress < 0)
            progress = 0;
        if (progress > 1)
            progress = 1;

        int startRed;
        int endRed;

        //if (Color.red(startColor) > Color.red(endColor)) {
        startRed = Color.red(startColor);
        endRed = Color.red(endColor);
        //} else {
        //    startRed = Color.red(endColor);
        //    endRed = Color.red(startColor);
        //}

        int startGreen;
        int endGreen;

        //if (Color.green(startColor) > Color.green(endColor)) {
        startGreen = Color.green(startColor);
        endGreen = Color.green(endColor);
        //} else {
        //    startGreen = Color.green(endColor);
        //    endGreen = Color.green(startColor);
        //}

        int startBlue;
        int endBlue;

        //if (Color.blue(startColor) > Color.blue(endColor)) {
        startBlue = Color.blue(startColor);
        endBlue = Color.blue(endColor);
        //} else {
        //    startBlue = Color.blue(endColor);
        //    endBlue = Color.blue(startColor);
        //}


        float redDiff = endRed - startRed;
        float greenDiff = endGreen - startGreen;
        float blueDiff = endBlue - startBlue;

        int r = (int) (progress * redDiff + startRed);
        int g = (int) (progress * greenDiff + startGreen);
        int b = (int) (progress * blueDiff + startBlue);

        int returnColor = Color.rgb(r, g, b);
        return "#" + Integer.toHexString(returnColor);
    }

}
