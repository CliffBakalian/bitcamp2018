package edu.umcp.jacc.burnt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String,String> params = new HashMap<>();
        params.put("id","524901");
        params.put("APPID","06fd8ba1c2e632e5a69b97895d7cb44d");

        NetworkManager.getInstance(this).makeRequest(this, Request.Method.GET, headers, params, "http://api.openweathermap.org/data/2.5/forecast", new CustomListener<String>() {
            @Override
            public void getResult(String result) {
                // for testing
            }
        });
    }
}
