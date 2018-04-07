package edu.umcp.jacc.burnt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Informer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informer);

        String lat = "":
        String lon = "";

        NetworkManager.getInstance(this).makeRequest(this, Request.Method.GET, null, null,
                "https://api.darksky.net/forecast/d1e33e8fdffe9057c74d51f5a2de6e1d" + lat + "," + lon, new CustomListener<String>() {
                    @Override
                    public void getResult(String object) {
                        DataParser.parseUV(object);

                    }
                });
    }
}