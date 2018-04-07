package edu.umcp.jacc.burnt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.FaceDetector;
import android.support.annotation.NonNull;
import android.util.Log;

public class Model {
    private static final String TAG = "Model";
    public static CameraManager openCamera(Context c){
        String id="BACK";
        CameraManager manager = (CameraManager)c.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (manager != null) {
                for (String cameraId : manager.getCameraIdList()) {
                    CameraCharacteristics cc = manager.getCameraCharacteristics(cameraId);
                    Integer facing = cc.get(CameraCharacteristics.LENS_FACING);
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        // found the front-facing camera!
                        id = cameraId;
                    }
                }
                try {
                    manager.openCamera(id, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {

                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {

                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {

                        }
                    }, null);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return manager;
    }

    public static int manipulate(Bitmap bm){
        FaceDetector fd = new FaceDetector(bm.getWidth(),bm.getHeight(),1);
        FaceDetector.Face[] face = new FaceDetector.Face[1];
        if (fd.findFaces(bm,face) == 0) {
            Log.d(TAG, "Could not find a face!");
            return bm.getPixel(bm.getWidth()/2, bm.getHeight()/2);
        }
        float edist = face[0].eyesDistance();
        PointF point = new PointF();
        face[0].getMidPoint(point);
        point.set(point.x,point.y - (edist/2));
        int xPos = (int)point.x;
        int yPos = (int)point.y;
        int negOffset = xPos - (int)edist/2;
        int posOffset = xPos + (int)edist/2;
        int blue=0, red=0, green=0, alpha = 0;
        for (int i = negOffset; i <= posOffset; i++){
            blue += Color.blue(bm.getPixel(i,yPos));
            red += Color.red(bm.getPixel(i,yPos));
            green += Color.green(bm.getPixel(i,yPos));
            alpha += Color.alpha(bm.getPixel(i,yPos));
        }
        int avg = (posOffset-negOffset + 2);
        return Color.argb(alpha/avg,red/avg,green/avg,blue/avg);
    }

}
