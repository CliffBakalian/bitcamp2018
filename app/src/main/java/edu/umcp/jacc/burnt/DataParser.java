package edu.umcp.jacc.burnt;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {

    static int skinTones[] = {0xFFDFC4,0xEECEB3,0xE5C298,0xE5B887,0xE79E6D,0xCE967C,0xBA6C49,0xF0C8C9,0xB97C6D,0xAD6452,0xCB8442,0x704139,0x870400,0x430000,0x000000};

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

    public double parseLonAndLat(String latOrLon, String jsondata) {
        double result = -1;
        try {
            JSONObject obj = new JSONObject(jsondata);
            if (latOrLon == "lon") {
                result = obj.getDouble("longitude");
            } else if (latOrLon == "lat") {
                result = obj.getDouble("latitude");
            } else {
                throw new JSONException("Enter lat or lon");
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String skinType(int skinTone){

        String skinType = "";
        int count = 0;

        for (int i=0;i<skinTones.length;i++){
            if(skinTones[i] == skinTone)
                count = i;
        }

        if(count >= 0 && count <= 4)
            skinType = "Fair skin";
        else if(count > 4 && count <= 9)
            skinType = "Olive skin";
        else
            skinType = "Dark skin";

        return skinType;


    }

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

    public double uvValConverter(float uv, int rgbVal, int previousSunburns) {
        int skinColor = this.colorConverter(rgbVal);
        int medianTone = skinTones[7];
        float difference = 0;
        int level1 = 1;
        int level2 = 2;
        int i = 0;
        double percentage = 0;
        for (int elem: skinTones) {
            if (skinColor == elem) {
                difference = 7-i;
            }
            i++;
        }
        percentage = 0.025*i;
        if (previousSunburns == level1) {
            percentage+=0.05;
        } else if (previousSunburns == level2) {
            percentage+=0.1;
        }
        double experiencedUV = uv*(1+percentage);
        if (experiencedUV >= 0) {
            return experiencedUV;
        } else {
            return 0;
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
