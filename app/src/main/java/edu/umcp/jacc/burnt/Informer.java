package edu.umcp.jacc.burnt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.MapView;

public class Informer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informer);

        // get latitude and longitude data
        Point p = new MapView(this).getLocationDisplay().getLocation().getPosition();

        String url = "https://api.darksky.net/forecast/d1e33e8fdffe9057c74d51f5a2de6e1d" + p.getY() + "," + p.getX();
        // call to UV
        NetworkManager.getInstance(this).makeRequest(this, Request.Method.GET, null, null, url, new CustomListener<String>() {
            @Override
            public void getResult(String object) {
                //new DataParser().parseUV(object);
            }
        });
    }
}