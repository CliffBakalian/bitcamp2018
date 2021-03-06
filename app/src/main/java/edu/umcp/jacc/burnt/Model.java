package edu.umcp.jacc.burnt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Landmark;
import com.google.api.services.vision.v1.model.Position;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Model {

    private static final String TAG = "Model";

    /*
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
        return argb(alpha/avg,red/avg,green/avg,blue/avg);
    }
    */

    public static void google(Bitmap bm, final Context ctx) {
        final Bitmap temp = bm;
        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(), null);

        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer("AIzaSyAWxBl0hkjk4TehMWR07Y1TPfV1ihFchjA")); // yeet
        final Vision vision = visionBuilder.setApplicationName("Burnt").build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        class VisionTask extends AsyncTask<Object,Integer,Integer> {
            private byte[] photoData;

            @Override
            protected Integer doInBackground(Object... objects) {
                try {
                    photoData = IOUtils.toByteArray(inputStream);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image inputImage = new Image();
                inputImage.encodeContent(photoData);

                Feature desiredFeature = new Feature();
                desiredFeature.setType("FACE_DETECTION");

                AnnotateImageRequest request = new AnnotateImageRequest();
                request.setImage(inputImage);
                request.setFeatures(Collections.singletonList(desiredFeature));

                BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();

                batchRequest.setRequests(Collections.singletonList(request));
                BatchAnnotateImagesResponse batchResponse = null;
                try {
                    batchResponse = vision.images().annotate(batchRequest).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<FaceAnnotation> faces = null;
                if (batchResponse != null) {
                    faces = batchResponse.getResponses().get(0).getFaceAnnotations();
                    if (faces != null) {
                        FaceAnnotation face = faces.get(0);
                        List<Landmark> lst = face.getLandmarks();
                        Position leye = null, reye = null, nose = null;
                        for (Landmark l : lst) {
                            if (l.getType().equals("NOSE_TIP")) {
                                nose = l.getPosition();
                            }
                            if (l.getType().equals("RIGHT_EYE")) {
                                reye = l.getPosition();
                            }
                            if (l.getType().equals("LEFT_EYE")) {
                                leye = l.getPosition();
                            }
                        }
                        if (leye != null && reye != null && nose != null) {
                            int range = (int) Math.abs(leye.getX() - reye.getX()) + 2;

                            int start = (int) Math.abs((nose.getX() - (range / 2)));
                            int blue = 0, red = 0, green = 0, alpha = 0;
                            int end = 0;
                            if (start + range > temp.getWidth())
                                end = temp.getWidth();
                            else
                                end = start + range;
                            int starty = Math.round(nose.getY() - 5);
                            int endy = starty + 11;
                            for (int j = starty; j <= endy; j++) {
                                for (int i = start; i <= end; i++) {
                                    blue += Color.blue(temp.getPixel(i, j));
                                    red += Color.red(temp.getPixel(i, j));
                                    green += Color.green(temp.getPixel(i, j));
                                    alpha += Color.alpha(temp.getPixel(i, j));
                                }
                            }
                            //Log.d("BLUE_VALUE", ""+ blue);
                            //Log.d("RED_VALUE", ""+ red);
                            //Log.d("GREEN_VALUE", ""+ green);
                            //Log.d("ALPHA_VALUE", ""+ alpha);
                            //i want to return this value:
                            range = (endy - starty + 2) * (end - start + 2);
                            return Color.argb(alpha / range, red / range, green / range, blue / range);
                        }
                    }
                }
                return -1;
            }

            protected void onProgressUpdate(Integer... progress) {
                // nothing to do, yet?
            }

            protected void onPostExecute(Integer result) {
                Intent next = new Intent(ctx, Informer.class);
                next.putExtra("color", result);
                ctx.startActivity(next);
            }
        }

        new VisionTask().execute();
    }
}
