package edu.umcp.jacc.burnt;

import java.util.Map;
import org.json.*;

public class DataParser {

    public static float parseUV(String jsondata) {
        JSONObject obj = null;
        float uvVal = 0.0f;
        try {
            obj = new JSONObject(jsondata);
            uvVal = obj.getJSONArray("result").getInt(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return uvVal;
    }
}
