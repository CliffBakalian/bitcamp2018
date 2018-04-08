package edu.umcp.jacc.burnt;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {

    static int skinTones[] = {0xFFE5C8,0xFFDABE,0xFFCEB4,0xFFC3AA,0xF0B8A0,0xE1AC96,0xD2A18C,0xC39582,0xB48A78,
            0xA57E6E,0x967264,0x87675A,0x785C50,0x695046,0x5A453C,0x4B39320,0x3C2E28, 0x2D221E,0x000000};

    public float parseUV(String jsondata) {
        float result = -1;
        try {
            JSONObject obj = new JSONObject(jsondata);
            float uvVal = obj.getInt("uvIndex");
            return uvVal;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

//    public double parseLonAndLat(String latOrLon, String jsondata) {
//        double result = -1;
//        try {
//            JSONObject obj = new JSONObject(jsondata);
//            if (latOrLon.equals("lon")) {
//                result = obj.getDouble("longitude");
//            } else if (latOrLon.equals("lat")) {
//                result = obj.getDouble("latitude");
//            } else {
//                throw new JSONException("Enter lat or lon");
//            }
//            return result;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    private int colorConverter(int color) {
        String hex = String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
        int skinColor = Integer.parseInt(hex);
        int i = 0;
        int finalColor = 0;
        while (skinTones[i] >= skinColor) {
            i++;
        }
        if (skinTones.length-i == skinTones.length) {
            finalColor = skinTones[0];
        }
        else if (skinColor-skinTones[i] < skinTones[i-1]-skinColor) {
            finalColor = skinTones[i];
        } else {
            finalColor = skinTones[i-1];
        }
        return finalColor;
    }

    public double uvValConverter(float uv, int rgbVal) {
        int skinColor = colorConverter(rgbVal);
        int medianTone = skinTones[7];
        float difference = 0;
        int level1 = 1;
        int level2 = 2;
        int i = 0;
        double percentage = 0;
        for (int elem: skinTones) {
            if (skinColor == elem) {
                difference = (skinTones.length/2)-i;
            }
            i++;
        }
        percentage = 0.015*i;
        double experiencedUV = uv*(1+percentage);
        if (experiencedUV >= 0) {
            return experiencedUV;
        } else {
            return 0
                    ;
        }
    }
    public String exposureCategory(double experiencedUV) {
        String result = "";
        if (experiencedUV >= 0 && experiencedUV <= 2) {
            result = "Low";
        } else if (experiencedUV >= 3 && experiencedUV <= 5) {
            result = "Moderate";
        } else if (experiencedUV >= 6 && experiencedUV <= 7) {
            result = "High";
        } else if (experiencedUV >= 8 && experiencedUV <= 10) {
            result = "Very High";
        } else {
            result = "Extreme";
        }
        return result;
    }

}
