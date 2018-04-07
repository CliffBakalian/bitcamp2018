package edu.umcp.jacc.burnt;

import android.graphics.Color;

import java.util.Map;
import org.json.*;

public class DataParser {

    int skinTones[] = {0xFFDFC4,0xEECEB3,0xE5C298,0xE5B887,0xE79E6D,0xCE967C,0xBA6C49,0xF0C8C9,0xB97C6D,0xAD6452,0xCB8442,0x704139,0x870400,0x430000,0x000000};

    float uvIndex = 0;
    double lon = 0;
    double lat = 0;

    public DataParser (float uvIndex1,double lon1,double lat1) {
        uvIndex = uvIndex1;
        lon = lon1;
        lat = lat1;
    }

    public DataParser parseUV(String jsondata) throws JSONException {
        JSONObject obj = new JSONObject(jsondata);
        float uvVal = obj.getInt("uvIndex");
        double lonVal = obj.getDouble("lon");
        double latVal = obj.getDouble("lat");
        return new DataParser(uvVal,lonVal,latVal);
    }

    public int colorConverter(int color) {
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
