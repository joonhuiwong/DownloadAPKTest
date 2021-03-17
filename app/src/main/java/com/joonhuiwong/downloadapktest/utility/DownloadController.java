package com.joonhuiwong.downloadapktest.utility;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.joonhuiwong.downloadapktest.BuildConfig;
import com.joonhuiwong.downloadapktest.R;

import java.io.File;

public class DownloadController {

    public static final String FILE_NAME = "SampleDownloadApp.apk";
    public static final String FILE_BASE_PATH = "file://";
    public static final String MIME_TYPE = "application/vnd.android.package-archive";
    public static final String PROVIDER_PATH = ".provider";
    public static final String APP_INSTALL_PATH = "\"application/vnd.android.package-archive\"";

    private Context context;
    private String url;

    public DownloadController(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    public void cleanUp() {
        String destination = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        destination += FILE_NAME;

        File file = new File(destination);

        if (file.exists()) {
            file.delete();
        }
    }

    public void enqueueDownload() {
        String destination = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        destination += FILE_NAME;

        Uri uri = Uri.parse(FILE_BASE_PATH + destination);

        File file = new File(destination);

        if (file.exists()) {
            file.delete();
        }

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setMimeType(MIME_TYPE);
        request.setTitle(context.getString(R.string.title_file_download));
        request.setDescription(context.getString(R.string.downloading));

        // Set Destination
        request.setDestinationUri(uri);

        showInstallOption(destination, uri);

        // Enqueue a new download and show the referenceId
        downloadManager.enqueue(request);

        Toast.makeText(context, context.getString(R.string.downloading), Toast.LENGTH_SHORT)
                .show();
    }

    private void showInstallOption(String destination, Uri uri) {

        BroadcastReceiver onComplete = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(
                            context,
                            BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                            new File(destination)
                    );
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    install.setData(contentUri);

                    context.startActivity(install);
                    context.unregisterReceiver(this);
                    // finish()
                } else {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    install.setDataAndType(
                            uri,
                            APP_INSTALL_PATH
                    );

                    context.startActivity(install);
                    context.unregisterReceiver(this);
                    // finish()
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
