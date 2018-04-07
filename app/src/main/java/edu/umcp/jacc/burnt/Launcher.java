package edu.umcp.jacc.burnt;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.view.SurfaceView;
import android.widget.Button;
import java.util.concurrent.Semaphore;


public class Launcher extends AppCompatActivity {

    private Button btnCapture;
    private SurfaceView surfaceView;

    private String mCameraId;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }

        /*
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
        */
    }
}
