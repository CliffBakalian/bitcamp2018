package edu.umcp.jacc.burnt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.FaceDetector;
import android.support.annotation.NonNull;

public class Model {
    public void PicureStuff(Context c){
        String[] lst = new String[0];
        String id="BACK";
        CameraManager manager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
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
    }

    public void manipulate(Bitmap bm){
        FaceDetector fd = new FaceDetector(bm.getWidth(),bm.getHeight(),1);
        FaceDetector.Face[] face = new FaceDetector.Face[1];
        fd.findFaces(bm,face);
        float edist = face[0].eyesDistance();
        PointF midpoint = new PointF();
        face[0].getMidPoint(midpoint);
        midpoint.set(midpoint.x,midpoint.y - (edist/2));
        
        
    }

}
