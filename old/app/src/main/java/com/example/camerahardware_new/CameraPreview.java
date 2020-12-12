package com.example.camerahardware_new;


import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.test.runner.screenshot.Screenshot;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;



    // Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        //surfaceView = new SurfaceView(context);
        // addView(surfaceView);

        //preview = (SurfaceView) findViewById(R.id.surfaceView);
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setKeepScreenOn(true);

        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            //mCamera.open();
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("MyCameraApp", "camera prervierw should open");

            // left blank for now
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
       // mCamera.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
                               int width, int height) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(0);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("MyCameraApp", "surface chaged");
//
            // intentionally left blank for a test
        }
    }


//    private void takePhoto() {
//
//        // Create a bitmap the size of the scene view.
//        final Bitmap bitmap = Bitmap.createBitmap(Bitmap.Config.ARGB_8888, mSurfaceHolder);
//
//
//
//        // Create a handler thread to offload the processing of the image.
//        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
//        handlerThread.start();
//        // Make the request to copy.
//        PixelCopy.request(mSurfaceHolder.lockCanvas(), bitmap, (copyResult) -> {
//            if (copyResult == PixelCopy.SUCCESS) {
//                Log.e(TAG,bitmap.toString());
//                String name = String.valueOf(System.currentTimeMillis() + ".jpg");
//                imageFile = ScreenshotUtils.store(bitmap,name);
//
//            } else {
//                Toast toast = Toast.makeText(getViewActivity(),
//                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
//                toast.show();
//            }
//            handlerThread.quitSafely();
//        }, new Handler(handlerThread.getLooper()));
//    }


}

