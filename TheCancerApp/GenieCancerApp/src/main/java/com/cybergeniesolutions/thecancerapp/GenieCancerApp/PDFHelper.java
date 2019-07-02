package com.cybergeniesolutions.thecancerapp.GenieCancerApp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by sadafk on 27/04/2017.
 */

public class PDFHelper {

    private File file;
    private static final String TAG = "PDFHelper";
    private Context context;


    public PDFHelper(File file, Context context){
        this.file = file;
        this.context = context;

    }

    public void showPDF(){
        Log.d(TAG, "in showPdf()");

        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, "com.cybergeniesolutions.thecancerapp.GenieCancerApp.provider", file);
        intent.setDataAndType(uri, "application/pdf");
        context.startActivity(intent);
    }

    public void createFile(Activity activity){
        Log.d(TAG, "in createFile()");
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG,"Permission is granted");
                    file.createNewFile();
                } else {

                    Log.v(TAG,"Permission is revoked");
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
            else { //permission is automatically granted on sdk<23 upon installation
                Log.v(TAG,"Permission is granted");
                file.createNewFile();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createEmail(){
        Log.d(TAG, "in createFile()");
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Body Temperature Records");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        Uri uri = FileProvider.getUriForFile(context, "com.cybergeniesolutions.thecancerapp.GenieCancerApp.provider", file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Send mail..."));
    }



}