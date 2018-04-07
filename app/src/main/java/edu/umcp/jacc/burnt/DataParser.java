package edu.umcp.jacc.burnt;

import java.util.Map;
import org.json.*;

public class DataParser {

    public float parseUV(String jsondata) throws JSONException {
        JSONObject obj = new JSONObject(jsondata);
        float uvVal = obj.getJSONArray("result").getInt(2);
        return uvVal;
    }

    public float calcUV(float elevation, float cloudCoverage, float ozone){

    }
}
