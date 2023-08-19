package com.ayan.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private String TAG = "MainActivity";
    ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        }

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