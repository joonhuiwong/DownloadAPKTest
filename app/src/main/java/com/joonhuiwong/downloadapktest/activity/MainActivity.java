package com.joonhuiwong.downloadapktest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.joonhuiwong.downloadapktest.R;
import com.joonhuiwong.downloadapktest.utility.DownloadController;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_STORAGE = 0;

    DownloadController downloadController;

    Button buttonDownload;
    ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample APK
        String apkUrl = "https://github.com/joonhuiwong/my_library/releases/download/0.5-pre-release/my_library_v0.5.apk";

        downloadController = new DownloadController(this, apkUrl);

        buttonDownload = findViewById(R.id.buttonDownload);
        mainLayout = findViewById(R.id.mainLayout);

        buttonDownload.setOnClickListener(v -> {
            // check storage permission granted if yes then start downloading file
            checkStoragePermission();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkStoragePermission() {
        // Check if the storage permission has been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Start Download
            downloadController.enqueueDownload();
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar snackbar = Snackbar.make(mainLayout, R.string.storage_access_required, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> ActivityCompat.requestPermissions(
                            this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE
                    ));
            snackbar.show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE
            );
        }
    }
}