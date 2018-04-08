package edu.umcp.jacc.burnt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Informer extends AppCompatActivity implements LocationListener, View.OnClickListener {

    private static final String TAG = "Informer";

    private int color;
    private Criteria crit = new Criteria();
    private boolean flag = true;
    private LocationManager locationManager;

    public void checkLocationPermission() {
        Log.d(TAG, "checkLocationPermission");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location")
                        .setMessage("We need location permissions to find where you will be!")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Informer.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 99: {
                Log.d(TAG, "case 99");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission granted");
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        Log.d(TAG, "putting in location request");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
                    }

                }  // else permission denied, boo!
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informer);

        findViewById(R.id.back).setOnClickListener(this);

        color = getIntent().getIntExtra("color", 0xFFFF);
        findViewById(R.id.back).setBackgroundTintList(ColorStateList.valueOf(color));
        Log.d(TAG, "begun -- color = " + Integer.toHexString(color));

        // findViewById(R.id.background).setBackgroundColor(color);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        crit.setAccuracy(Criteria.ACCURACY_MEDIUM);

        final LocationListener ll = this;

        checkLocationPermission();

        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                locationManager.removeUpdates(ll);
                if (flag) // then run locator through input zip-code
                    zip();
            }
        };
        h.postDelayed(r,1000 * 10); // cancel at 10 seconds
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null)
                locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this); // done
        flag = false;
        api(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled");
    }

    private void zip() {
        // Special thanks to esri!
        LocatorTask loc = new LocatorTask("http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");
        final ListenableFuture<List<GeocodeResult>> lf = loc.geocodeAsync("College Park, MD 20742", new GeocodeParameters());
        lf.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GeocodeResult> list = lf.get();
                    Point p = list.get(0).getInputLocation();
                    api(p.getY(), p.getX());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void api(double lat, double lon) {
        String url = "https://api.darksky.net/forecast/d1e33e8fdffe9057c74d51f5a2de6e1d/" + lat + "," + lon;
        Log.d(TAG, url);

        // call to UV
        NetworkManager.getInstance(this).makeRequest(this, Request.Method.GET, null, null, url, new CustomListener<String>() {
            @Override
            public void getResult(String object) {
                final int res = DataParser.parseUV(object);
                final double fin = DataParser.uvValConverter(res, color);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.progress).setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.uv)).setText(String.format(getResources().getString(R.string.uv_display), res, DataParser.exposureCategory(res)));
                        ((TextView) findViewById(R.id.output_info)).setText(String.format(getResources().getString(R.string.you_ind), fin, DataParser.exposureCategory(fin)));
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
            finish();
        }
    }
}