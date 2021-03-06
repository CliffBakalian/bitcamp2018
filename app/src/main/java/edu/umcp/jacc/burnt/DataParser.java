package edu.umcp.jacc.burnt;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {

    private static int skinTones[] = {0xFFE5C8,0xFFDABE,0xFFCEB4,0xFFC3AA,0xF0B8A0,0xE1AC96,0xD2A18C,0xC39582,0xB48A78,
            0xA57E6E,0x967264,0x87675A,0x785C50,0x695046,0x5A453C,0x4B39320,0x3C2E28, 0x2D221E,0x000000};

    public static int parseUV(String jsondata) {
        int result = 0;
        try {
            JSONObject obj = new JSONObject(jsondata);
            JSONArray newArr = obj.getJSONObject("daily").getJSONArray("data");
            result = ((JSONObject) newArr.get(0)).getInt("uvIndex");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Log.d("UV_INDEX",""+result);
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

    private static int colorConverter(int color) {
        int skinColor = color & (0x00ffffff);
        int i = 0;
        int finalColor;
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

    public static double uvValConverter(float uv, int rgbVal) {
        int skinColor = colorConverter(rgbVal);
        float difference = 0;
        int i = 0;
        double percentage;
        for (int elem: skinTones) {
            if (skinColor == elem) {
                difference = (skinTones.length/2)-i;
            }
            i++;
        }
        percentage = 0.05*difference;
        double experiencedUV = uv*(1+percentage);
        if (experiencedUV >= 0) {
            return experiencedUV;
        } else {
            return 0;
        }
    }
    public static String exposureCategory(double experiencedUV) {
        String result = "";
        if (experiencedUV < 2.5) {
            result = "Low";
        } else if (experiencedUV >= 2.5 && experiencedUV < 5.5) {
            result = "Moderate";
        } else if (experiencedUV >= 5.5 && experiencedUV < 7.5) {
            result = "High";
        } else if (experiencedUV >= 7.5 && experiencedUV < 10.5) {
            result = "Very High";
        } else {
            result = "Extreme";
        }
        return result;
    }

}
