package edu.umcp.jacc.burnt;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.hardware.camera2.*;
import android.hardware.camera2.CameraCharacteristics;
import android.media.FaceDetector;
import android.support.annotation.NonNull;

import static android.hardware.camera2.CameraCharacteristics.LENS_FACING;

public class Model {
    public void PicureStuff(Context c){
        String[] lst;
        String id="BACK";
        CameraManager manager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
        try {
            lst = manager.getCameraIdList();
            for(int i = 0; i< lst.length; i++) {
                CameraCharacteristics cc = manager.getCameraCharacteristics(lst[i]);
                if (cc == null)
                    throw new NullPointerException("No camera with id " + lst[i]);
                if (cc.LENS_FACING.equals(CameraMetadata.LENS_FACING_FRONT))
                    id = lst[i];
            }
            try{
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
            }
            catch (SecurityException e){
                lst = null;
            }
        } catch (CameraAccessException e) {
            lst = null;
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
