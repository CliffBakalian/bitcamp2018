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

        Map<String, String> params = new HashMap<>();
        //params.put("id","524901");
        //params.put("APPID","06fd8ba1c2e632e5a69b97895d7cb44d");

        NetworkManager.getInstance(this).makeRequest(this, Request.Method.GET, null,
                null, "https://api.darksky.net/forecast/d1e33e8fdffe9057c74d51f5a2de6e1d", new CustomListener<String>() {
                    @Override
                    public void getResult(String object) {
                        DataParser.parseUV(object);

                    }
                });
    }
}