package com.example.ui1;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
//import android.hardware.camera2.*;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.util.SparseIntArray;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import androidx.test.runner.screenshot;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISSION = 1001;
    private static final SparseIntArray ORIENTATION = new SparseIntArray();


    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private String mVideoUrl = "";

    private MediaRecorder mMediaRecorder;

    private int mScreenDensity;
    private static final int DISPLAY_WIDTH = 1080;
    private static final int DISPLAY_HEIGHT = 1920;

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    private RelativeLayout mRootLayout;
    private ToggleButton mToggleButton;
    //    private VideoView mVideoView;
    ImageButton B1,B2;
    EditText E1;
    //    ViewGroup V1;
    private final static int START_DRAGGING = 0;
    private final static int STOP_DRAGGING = 1;
    private int status;
    int flag=0;
    float xAxis = 0f;
    float yAxis = 0f;
    float lastXAxis = 0f;
    float lastYAxis = 0f;
    int numberOfLines=0;
    int i=0;


    private Camera mCamera;
    private CameraPreview mCameraPreview;
    boolean cam;
    private String currentPhotoPath="default path";
//    SurfaceView preview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToggleButton = findViewById(R.id.toggleButton);
        mRootLayout = findViewById(R.id.Relative_Layout);
        B1 = (ImageButton) findViewById(R.id.imageButton2);
        B2 = (ImageButton) findViewById(R.id.imageButton);
        E1 = (EditText) findViewById(R.id.editTextTextPersonName);
        E1.setVisibility(View.GONE);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        mScreenDensity = displayMetrics.densityDpi;
//
//        mMediaRecorder = new MediaRecorder();
//        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);
        mToggleButton = findViewById(R.id.toggleButton);
        mRootLayout = findViewById(R.id.Relative_Layout);
        B1 = (ImageButton) findViewById(R.id.imageButton2);
        B2 = (ImageButton) findViewById(R.id.imageButton);
        E1 = (EditText) findViewById(R.id.editTextTextPersonName);
        E1.setVisibility(View.GONE);

//        V1 = (RelativeLayout) findViewById(R.id.Relative);
        mCamera = getCameraInstance();
        cam = checkCameraHardware(this);
        mCameraPreview = new CameraPreview(this, mCamera);


        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_Preview);


        preview.addView(mCameraPreview);
//



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;

        mMediaRecorder = new MediaRecorder();
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Log.d("data"," Identifng the error"+"    "+mMediaProjectionManager);

//        mVideoView = findViewById(R.id.videoView);
//        mToggleButton = findViewById(R.id.toggleButton);
//        mRootLayout = findViewById(R.id.rootLayout);

        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)){
                        mToggleButton.setChecked(false);
                        Snackbar.make(mRootLayout,"Permission", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Enable", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]
                                                        {
                                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                Manifest.permission.RECORD_AUDIO
                                                        },
                                                REQUEST_PERMISSION );

                                    }
                                });

                    }else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]
                                        {
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.RECORD_AUDIO
                                        },
                                REQUEST_PERMISSION );
                    }
                } else {
                    toggleScreenShare(v);
                }
            }
        });






















        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                if(E1.getVisibility()==View.GONE){
                    E1.setVisibility(View.VISIBLE);
                }

            }
        });
//        share1 = (Button) findViewById(R.id.share);
//        share1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap bitmap = takeScreenshot();
//                try {
//                    saveBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
















        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Log.d("MyCameraApp", "mcamera access"+"   "+mCamera);
                Log.d("MyCameraApp", "camera open"+"   "+cam);
//                Log.d("MyCameraApp", "path"+"   "+currentPhotoPath)

                //mCameraPreview.surfaceCreated(mCameraPreview.mSurfaceHolder);

//                mCamera.takePicture(null, null, mPicture);
//                Log.d("MyCameraApp", "path"+"   "+currentPhotoPath);
                try {

                    mCamera.startPreview();
                    mCamera.takePicture(null, null, mPicture);
                    Log.d("MyCameraApp", "path"+"   "+currentPhotoPath);
                }
                catch (Exception e){

                    Log.d("kiran find","problem in take picture");
                }


            }
        });
        E1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if(me.getAction()==MotionEvent.ACTION_DOWN){
                    status = START_DRAGGING;
                    final float x = me.getX();
                    final float y = me.getY();
                    lastXAxis = x;
                    lastYAxis = y;
                    v.setVisibility(View.INVISIBLE);
                }else if(me.getAction()==MotionEvent.ACTION_UP){
                    status = STOP_DRAGGING;
                    flag=0;
                    v.setVisibility(View.VISIBLE);
                }else if(me.getAction()==MotionEvent.ACTION_MOVE){
                    if (status == START_DRAGGING){
                        flag=1;
                        v.setVisibility(View.VISIBLE);
                        final float x = me.getX();
                        final float y = me.getY();
                        final float dx = x - lastXAxis;
                        final float dy = y - lastYAxis;
                        xAxis += dx;
                        yAxis += dy;
                        v.setX((int)xAxis);
                        v.setY((int)yAxis);
                        v.invalidate();
                    }
                }
                return false;
            }
        });
    }























    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to aceess cameara.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
        return camera;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = null;
            try {
                pictureFile = getOutputMediaFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Input file problem.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                // finish();
                e.printStackTrace();
            }
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
//                finish();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Input file problem.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "input exception0.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    };
    private File getOutputMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
//

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    public void saveBitmap(Bitmap bitmap) throws IOException {

        File imagePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "JPEG_" + "_";
        File kiran = File.createTempFile(imageFileName,".png",imagePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(kiran);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage()+"kiafdksdnfsfksznvks", e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage()+"file not found", e);
        }
    }


    private void toggleScreenShare(View v) {
        ToggleButton toggleButton = (ToggleButton) v;
        if (toggleButton.isChecked()){
            initRecorder();
            recordScreen();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            stopRecordScreen();

//            mVideoView.setVisibility(View.VISIBLE);
//            mVideoView.setVideoURI(Uri.parse(mVideoUrl));
//            mVideoView.start();
        }

    }

    private void recordScreen() {
        if (mMediaProjection == null){
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(),REQUEST_CODE);
            return;
        }

        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private VirtualDisplay createVirtualDisplay(){
        return mMediaProjection.createVirtualDisplay("MainActivity", DISPLAY_WIDTH,DISPLAY_HEIGHT,
                mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(),
                null,null);
    }

    private void initRecorder() {
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);


            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "CameraAnnotations/ScreenRecordings");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("App", "failed to creMediaProjectionManagerServiceate directory");
                }
            }

            File picStorageDir = new File(Environment.getExternalStorageDirectory(), "CameraAnnotations/Snapshots");

            if (!picStorageDir.exists()) {
                if (!picStorageDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }

            mVideoUrl = mediaStorageDir +
                    new StringBuilder("/CamAnotRecord-").append(new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss")
                            .format(new Date())).append(".mp4").toString();

            mMediaRecorder.setOutputFile(mVideoUrl);
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH,DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(512*10000);
            mMediaRecorder.setVideoFrameRate(60);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATION.get(rotation+90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("resultCode"," Identifng the error"+"    "+resultCode);
        Log.d("data"," Identifng the error"+"    "+data);


        if(requestCode != REQUEST_CODE){
            Toast.makeText(this, "Unk error", Toast.LENGTH_SHORT).show();
            return;
        }
        if(resultCode != RESULT_OK){
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
        mMediaProjectionCallback = new MediaProjectionCallback();

        Log.d("data"," Identifng the error"+"    "+mMediaProjectionCallback);

        try {
            mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Log.d("data"," Identifng the error"+"    "+mMediaProjection);


        mMediaProjection.registerCallback(mMediaProjectionCallback,null);

        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
        Log.d("Error"," this is media"+"     "+ mMediaProjection);
    }






    private class MediaProjectionCallback extends MediaProjection.Callback {

        @Override
        public void onStop() {
            super.onStop();

            if (mToggleButton.isChecked()){
                mToggleButton.setChecked(false);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
            }

            mMediaProjection = null;
            stopRecordScreen();
        }
    }

    private void stopRecordScreen() {
        if(mVirtualDisplay == null)
            return;

        mVirtualDisplay.release();
        destroyMediaProjection();
    }

    private void destroyMediaProjection() {
        if (mMediaProjection != null){
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_PERMISSION:{
                if (grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    toggleScreenShare(mToggleButton);
                }else
                {
                    mToggleButton.setChecked(false);
                    Snackbar.make(mRootLayout,"Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]
                                                    {
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                            Manifest.permission.RECORD_AUDIO
                                                    },
                                            REQUEST_PERMISSION );

                                }
                            });
                }
                return;
            }
        }
    }



}