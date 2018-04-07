package edu.umcp.jacc.burnt;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.esri.android.map.MapView;

public class Informer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informer);

        // get latitude and longitude data
        Location l = new MapView(this).getLocationDisplayManager().getLocation();

        String url = "https://api.darksky.net/forecast/d1e33e8fdffe9057c74d51f5a2de6e1d" + l.getLatitude() + "," + l.getLongitude();
        // call to UV
        NetworkManager.getInstance(this).makeRequest(this, Request.Method.GET, null, null, url, new CustomListener<String>() {
            @Override
            public void getResult(String object) {
                //DataParser.parseUV(object);
            }
        });
    }
}