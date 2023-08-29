package com.ayan.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.net.URI;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private String TAG = "MainActivity";
    ImageView qrImage;

    MaterialButton btnScan, btnOpenCamera, btnOpenGallery;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 100;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private BarcodeScanner barcodeScanner;
    private BarcodeScannerOptions barcodeScannerOptions;

    private Uri imageURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // btnScan.findViewById(R.id.btnScanGallery);
        // qrImage.findViewById(R.id.imageview);

        btnScan = findViewById(R.id.btnScanGallery);
        qrImage = findViewById(R.id.imageview);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        btnOpenGallery = findViewById(R.id.btnOpenGallery);


        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageGallery();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detectResultFromImage();

            }
        });

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageCamera();
            }
        });






/*
        qrImage = findViewById(R.id.qrImage);


        Bitmap bitmap;
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        QRGEncoder qrgEncoder = new QRGEncoder("01741753671", null, QRGContents.Type.PHONE, 100);
        qrgEncoder.setColorBlack(Color.WHITE);
        qrgEncoder.setColorWhite(Color.BLACK);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }*/

/*        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });*/
    }

    private void detectResultFromImage(){
        
        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageURI);
            Task<List<Barcode>> barcodeResult = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {

                            extractBarCodeQRCodeInfo(barcodes);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed scanning due to  "+e, Toast.LENGTH_SHORT).show();
                        }
                    });
            
        }catch (Exception exp){
            Toast.makeText(this, "Exception Occures!!", Toast.LENGTH_SHORT).show();
            exp.printStackTrace();
        }
    }

    private void extractBarCodeQRCodeInfo(List<Barcode> barcodes) {

        for(Barcode barcode :barcodes){
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();
            int barcodeType = barcode.getValueType();

            switch (barcodeType){
                case Barcode.TYPE_WIFI:
                    Barcode.WiFi typeWifi = barcode.getWifi();
                    Log.e(TAG, "Wifi Name : "+typeWifi.getSsid());
                    Log.e(TAG, "Wifi Password : "+typeWifi.getPassword());
                    Log.e(TAG, "Wifi Encryption Type : "+typeWifi.getEncryptionType());
            }

        }
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageURI = data.getData();

                        qrImage.setImageURI(imageURI);
                    } else {
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    public void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Sample_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample_Image_Description");

        imageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);

        cameraActivityResultLauncher.launch(intent);


    }

    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        qrImage.setImageURI(imageURI);
                    } else {

                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                    }

                }
            }
    );

    private boolean checkStoragePermission(){
        boolean result;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            result  = true;
        }else {
            result = false;
        }
        return result;
    }
    private boolean checkCameraPermission(){
        boolean result;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            result  = true;
        }else {
            result = false;
        }
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        // mCodeScanner.releaseResources();
        super.onPause();
    }
}