package edu.umcp.jacc.burnt;
import android.content.Context;
import android.hardware.camera2.*;
import android.hardware.camera2.CameraCharacteristics;
import android.support.annotation.NonNull;

import static android.hardware.camera2.CameraCharacteristics.LENS_FACING;

public class Model {
    public void PicureStuff(Context c){
        String[] lst;
        String id;
        CameraManager manager = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);
        try {
            lst = manager.getCameraIdList();
            for(int i = 0; i< lst.length; i++) {
                CameraCharacteristics cc= manager.getCameraCharacteristics(lst[i]);
                if (cc == null)
                    throw new NullPointerException("No camera with id " + lst[i]);
                if (cc.LENS_FACING.equals(CameraMetadata.LENS_FACING_FRONT))
                    id = lst[i];
            }
            manager.openCamera(id, new CameraDevice.StateCallback(),null);
        } catch (CameraAccessException e) {
            lst = null;
        }
    }



}
