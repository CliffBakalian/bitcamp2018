package edu.umcp.jacc.burnt;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

public class Launcher extends AppCompatActivity {
    private Button btnCapture;
    private SurfaceView surfaceView;

    private String mCameraId;

    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Model.openCamera(getApplicationContext());
        surfaceView = findViewById(R.id.cam_receiver);

        btnCapture = findViewById(R.id.camera);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
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

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    }

    private void takePicture() {
        if (cameraDevice == null)
            return;
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics cc = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] picSize = null;
            if (cc != null)
                picSize = cc.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
        }catch (CameraAccessException e){

        }
    }
}
